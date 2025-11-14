package com.restaurant.ordering_backend.service;

import com.restaurant.ordering_backend.dto.OrderRequestItem;
import com.restaurant.ordering_backend.model.Order;
import com.restaurant.ordering_backend.model.OrderItem;
import com.restaurant.ordering_backend.model.Product;
import com.restaurant.ordering_backend.repository.OrderRepository;
import com.restaurant.ordering_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 引入“事务”

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OrderService 的具体实现类。
 * (“订单大厨”本人)
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository; // “订单大厨”需要“菜品仓库”来查价格

    /**
     * (事务管理)
     * @Transactional 注解：
     * 这是一个“事务”注解。它保证 createOrder() 方法中的所有数据库操作，
     * 要么“全部成功”，要么“全部失败回滚”。
     * (防止出现“订单创建了，但订单项没保存成功”的“脏数据”)
     */
     @Override
     @Transactional // (非常重要!)
    public Order createOrder(List<OrderRequestItem> requestItems) {

         //创建一个新的Order对象
         Order newOrder = new Order();
         newOrder.setCreateAt(LocalDateTime.now());
         newOrder.setStatus("PENDING");

         List<OrderItem> orderItems = new ArrayList<>();
         double totalPrice = 0.0;
         //遍历购物车的每一项
         for (OrderRequestItem orderRequestItem : requestItems) {
             // 1. (业务逻辑) 检查菜品是否存在
             Product product = productRepository.findById(orderRequestItem.getProductId())
                     .orElseThrow(() -> new RuntimeException("菜品未找到 (ID: " + orderRequestItem.getProductId() + ")"));

             // 2. (业务逻辑) 检查菜品是否可售
             if(!product.getAvailable()){
                 throw new RuntimeException("菜品已下架 (Name: " + product.getName() + ")");
             }
             //（安全！）使用数据库中的“product.getPrice()”，而不是前端传来的价格
             double unitPrice = product.getPrice();
             int quantity = orderRequestItem.getQuantity();

             //创建一个新的OrderItem（订单项）
             OrderItem orderItem = new OrderItem();
             orderItem.setProduct(product);
             orderItem.setQuantity(quantity);
             orderItem.setUintPrice(unitPrice);//存入“快照”单价

             //（关键！）建立“双向”关联
             orderItem.setOrder(newOrder);//告诉订单项，你属于newOrder
             orderItems.add(orderItem);//把订单项加入列表

             totalPrice += (unitPrice * quantity);
         }

         //设置Order的最终总价和订单项列表
         newOrder.setTotalPrice(totalPrice);
         newOrder.setItems(orderItems);//告诉订单，其包含了这些orderItems

         // 3. (业务逻辑) 保存 Order (并级联保存所有 OrderItem)
         return orderRepository.save(newOrder);
     }

     @Override
     public List<Order> getAllOrders() {
         return orderRepository.findAll();
     }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * (功能：后台管理)
     */
    @Override
    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        // 1. (业务逻辑) 检查订单是否存在
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单未找到 (ID: " + id + ")"));

        // 2. (业务逻辑) 检查状态是否合法 (未来可以做得更复杂)
        if (status == null || (!status.equals("COMPLETED") && !status.equals("CANCELLED"))) {
            throw new RuntimeException("无效的订单状态: " + status);
        }

        // 3. 更新状态并保存
        order.setStatus(status);
        return orderRepository.save(order);
    }
}

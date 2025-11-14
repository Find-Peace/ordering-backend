package com.restaurant.ordering_backend.service;

import com.restaurant.ordering_backend.dto.OrderRequestItem;
import com.restaurant.ordering_backend.model.Order;
import org.springframework.http.ResponseEntity; // 引入 ResponseEntity

import java.util.List;
import java.util.Optional;


/**
 * 订单 (Order) 模块的业务逻辑服务接口。
 * (这是“订单大厨”的“菜谱”)
 */
public interface OrderService {

    /**
     * 创建一个新订单。
     * 这是最核心的业务逻辑。
     * @param requestItems
     * @return 返回创建成功的订单
     * @throws RuntimeException 如果菜品不存在或库存不足 (未来)
     */
    Order createOrder(List<OrderRequestItem> requestItems);

    /**
     * 获取所有订单 (后台管理)
     * @return 订单列表
     */
    List<Order> getAllOrders();

    /**
     * 根据 ID 查找订单
     * @param id 订单 ID
     * @return Optional 包装的订单对象
     */
    Optional<Order> getOrderById(Long id);

    /**
     * (新功能) 更新订单状态
     * (例如：从 PENDING 更新为 COMPLETED)
     * @param id 订单 ID
     * @param status 新的状态字符串
     * @return 更新后的订单
     * @throws RuntimeException 如果订单不存在或状态不合法
     */
    Order updateOrderStatus(Long id, String status);
}

package com.restaurant.ordering_backend.controller;

import com.restaurant.ordering_backend.dto.OrderRequestItem;
import com.restaurant.ordering_backend.model.Product;
import com.restaurant.ordering_backend.model.Order;
import com.restaurant.ordering_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单 (Order) 模块的控制器 (Controller)。
 * (这是“前台服务员”)
 * * 职责：
 * 1. 接收 HTTP 请求 (如 /api/orders)。
 * 2. 将业务逻辑“委托”给 OrderService (大厨)。
 * 3. 将 Service 的返回结果打包成 HTTP 响应。
 * 4. (新增) 处理 Service 抛出的异常 (如“菜品未找到”)。
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // "服务员" 只依赖 "订单大厨"
    @Autowired
    private OrderService orderService;


    /**
     * (C) 创建一个新订单
     * @param requestItems
     * @return 200 OK (带订单数据) 或 400 Bad Request (如果菜品不存在)
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody List<OrderRequestItem> requestItems) {

        // "服务员" 只管调用 "大厨" 的 "createOrder" 方法
        // "大厨" 会负责所有计算和检查
        try {
            Order savedOrder = orderService.createOrder(requestItems);
            return ResponseEntity.ok(savedOrder);
        } catch (RuntimeException e) {
            // 如果 "大厨" (Service) 抛出异常 (如“菜品未找到”或“已下架”)
            // "服务员" 负责将其转换为 400 Bad Request 错误
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     *（R）查询所有订单（方便后台管理）
     * @return 订单列表
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * (U) 更新订单状态 (新功能)
     * (供后厨或前台使用，例如：标记为 "已完成")
     *
     * @param id 订单 ID (来自 URL 路径)
     * @param statusPayload
     * @return 200 OK (带更新后的订单) 或 400/404 错误
     *
     * (使用 @PatchMapping，因为它只更新订单的“部分”属性——状态)
     * (我们使用 Map<String, String> 来接收简单的 {"status": "COMPLETED"} JSON)
     */
     @PatchMapping("/{id}/status")
     public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                    @RequestBody Map<String, String> statusPayload) {
         try{
             String status = statusPayload.get("status");
             Order updateOrder = orderService.updateOrderStatus(id, status);
             return ResponseEntity.ok(updateOrder);
         } catch (RuntimeException e) {
             // 如果 Service 抛出异常 (如“订单未找到”或“无效状态”)
             // 我们返回 400 Bad Request 或 404 Not Found
             // (更精细的错误处理我们稍后可以再加)
             if (e.getMessage().contains("未找到")) {
                 return ResponseEntity.notFound().build();
             }
             return ResponseEntity.badRequest().body(null);
         }
     }

}


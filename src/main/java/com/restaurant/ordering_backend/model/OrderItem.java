package com.restaurant.ordering_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // 引入 JsonIgnore
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//订单项ID

    @Column(nullable = false)
    private Integer quantity;//数量

    @Column(nullable = false)
    private Double uintPrice;//单价（保存时从Product复制过来，防止菜品涨价影响历史订单）

    // --- [ 关系 1: 这个订单项属于哪个菜品？ ] ---
    /**
     * "多对一": 多个 OrderItem (比如 "张三的宫保鸡丁" "李四的宫保鸡丁")
     * 对应 "一" 个 Product (菜品 "宫保鸡丁")
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // --- [ 关系 2: 这个订单项属于哪张订单？ ] ---
    /**
     * "多对一": 多个 OrderItem (2份宫保鸡丁, 1份煎扒鱼)
     * 对应 "一" 个 Order (订单 #1001)
     */
    @ManyToOne(fetch = FetchType.LAZY)// LAZY: 查询订单项时，默认不查询所属订单
    @JoinColumn(name = "order_id")
    @JsonIgnore//（重要！）防止“循环引用”
    private Order order;
}

package com.restaurant.ordering_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime; // 引入 Java 8 时间 API
import java.util.List; // 引入 List

@Entity
@Data
@Table(name = "orders") //“order”是SQL关键字，用复数作为表名更安全

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//订单ID

    @Column(nullable = false)
    private Double totalPrice;//订单总价，由后端计算

    @Column(nullable = false)
    private String status = "PENDING"; //订单状态（PENDING，COMPLETED，CANCELLED）

    private LocalDateTime createAt = LocalDateTime.now(); //订单创建时间

    // --- [ 以下是“一对多”关系的核心 ] ---

    /**
     * @OneToMany: “一对多”关系。一个Order对应多个OrderItem。
     * “mappedBy = "order"”：告诉JPA，这个关系的“外键“维护权在OrderItem类的”order”字段上。
     * * CascadeType.ALL:
     * （级联操作）这是一个重要设置！它表示：
     * 当我们“save”一个order时，请自动“save”其所包含的所有OrderItem。
     */
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> items;
}

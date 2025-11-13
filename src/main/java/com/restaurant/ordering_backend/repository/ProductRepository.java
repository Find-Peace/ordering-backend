package com.restaurant.ordering_backend.repository;

import com.restaurant.ordering_backend.model.Product; // 引入我们上一步定义的“菜品蓝图”
import org.springframework.data.jpa.repository.JpaRepository;

// 注意：这里是 "interface" (接口)，不是 "class" (类)
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 接口里是空的，不需要写任何代码
    // Spring Boot 会在“幕后”自动帮我们实现所有功能
}
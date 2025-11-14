package com.restaurant.ordering_backend.repository;

import com.restaurant.ordering_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// 继承 JpaRepository，管理 Category 实体，主键是 Long
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
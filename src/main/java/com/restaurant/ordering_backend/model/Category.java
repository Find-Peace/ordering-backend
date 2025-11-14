package com.restaurant.ordering_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 分类ID

    @Column(nullable = false, unique = true) // 分类名必须填写，且不能重复
    private String name; // 分类名称 (例如: "热菜")
}
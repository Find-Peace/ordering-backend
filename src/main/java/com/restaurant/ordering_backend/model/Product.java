package com.restaurant.ordering_backend.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 菜品的唯一编号

    @Column(nullable = false)
    private String name; // 菜品的名称

    private String description; // 菜品的描述

    @Column(nullable = false)
    private Double price; // 菜品的价格

    private String imageUrl; // 菜品照片的网址

    private Boolean available = true; // 菜品是否在售
}
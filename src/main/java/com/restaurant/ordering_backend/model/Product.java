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

    /**
     * @ManyToOne: “多对一”关系。多个Product对应一个Category。
     * Eager：（急加载）当查询菜品时，立刻把它的分类信息也一起查出来
     */
    @ManyToOne(fetch = FetchType.EAGER)

    /**
     * @JoinColumn:制定外键列。
     * JAP 会在“product”表中自动创建一个名为“category_id" 的列。
     * 用来存储它所属Category的ID。
     */
    @JoinColumn(name = "category_id")
    private Category category;
}
package com.restaurant.ordering_backend.dto;

import lombok.Data;

/**
 * DTO:用于接收前端购物车中的一项
 * 这是一个简单的Java类（POJO），不是数据库实体
 */
@Data
public class OrderRequestItem {
    private Long productId; //顾客想点的菜品ID
    private Integer quantity; //想点的数量
}

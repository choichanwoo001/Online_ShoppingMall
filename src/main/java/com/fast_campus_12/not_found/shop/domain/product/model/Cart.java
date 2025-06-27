package com.fast_campus_12.not_found.shop.domain.product.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Cart {
    private String id;
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private List<CartItem> cartItemList;  // 계층 관계 포함
}
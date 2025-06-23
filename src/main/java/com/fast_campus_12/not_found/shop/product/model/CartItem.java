package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;

import java.util.Date;

@Data
public class CartItem {
    private String id;
    private String cartId;
    private Long productId;
    private Long productVId;
    private int quantity;
    private Date updatedAt;// 계층 관계 포함
}
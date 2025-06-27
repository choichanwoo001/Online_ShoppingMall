package com.fast_campus_12.not_found.shop.domain.product.dto;

import lombok.Data;

@Data
public class CartItemViewDto {
    private String cartItemId;
    private Long productVariantId;
    private String productName;
    private String color;
    private String size;
    private String imageUrl;
    private String productUrl;
    private int quantity;
    private int price;
    private int discountPrice;
}

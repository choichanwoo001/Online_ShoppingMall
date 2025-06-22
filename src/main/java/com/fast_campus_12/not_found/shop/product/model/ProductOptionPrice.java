package com.fast_campus_12.not_found.shop.product.model;


import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class ProductOptionPrice {

    private BigInteger productVariantId;

    private Double originalPrice;

    private Double salePrice;

    private Double discountRate;

    private LocalDateTime field2;

    private LocalDateTime field3;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Double extraCharge;
}
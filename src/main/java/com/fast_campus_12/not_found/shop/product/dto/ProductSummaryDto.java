package com.fast_campus_12.not_found.shop.product.dto;


import lombok.*;

@Getter
@Builder
public class ProductSummaryDto {
    private Long id;
    private String title;
    private String thumbnail;
    private int price;
    private int reviewCount;
    private String comment;
}

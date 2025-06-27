package com.fast_campus_12.not_found.shop.domain.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryMenuDto {
    private String categoryName;
    private String categoryLink;
    private int priority;
}

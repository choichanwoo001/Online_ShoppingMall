package com.fast_campus_12.not_found.shop.domain.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductPageDto {
    private int totalCount;
    private List<ProductSummaryDto> items;
}

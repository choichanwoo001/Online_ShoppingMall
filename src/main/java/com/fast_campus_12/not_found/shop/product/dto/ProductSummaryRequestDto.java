package com.fast_campus_12.not_found.shop.product.dto;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSummaryRequestDto {
    private String category;
    private String subCategory;
    private int offset;
    private int limit;
    private ProductSortBy sortBy;
    private SortDirection sortDirection;
}

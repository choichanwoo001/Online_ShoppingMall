package com.fast_campus_12.not_found.shop.product.dto;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import com.fast_campus_12.not_found.shop.product.enums.SpecialProductCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSpecialSummaryRequestDto {
    private SpecialProductCategory specialProductCategory;
    private int offset;
    private int limit;
    private ProductSortBy sortBy;
    private SortDirection sortDirection;
}

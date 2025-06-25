package com.fast_campus_12.not_found.shop.product.dto.response;

import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductPageResponse {
    private PageResponseDto<ProductSummaryDto> pageResponseDto;
    private String category;
    private String subCategory;
    private String baseUrl;
    private String sortBy;
    private String sort;
    private String contentPath;
}
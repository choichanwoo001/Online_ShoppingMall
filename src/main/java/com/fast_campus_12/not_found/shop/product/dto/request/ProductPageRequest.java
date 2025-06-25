package com.fast_campus_12.not_found.shop.product.dto.request;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPageRequest {

    private int page = 1; // 기본값: 1페이지
    private ProductSortBy sortBy = ProductSortBy.PRICE; // 기본값: 가격 기준
    private SortDirection sort = SortDirection.ASC; // 기본값: 오름차순

    public int getOffset(int pageSize) {
        return (page - 1) * pageSize;
    }
}
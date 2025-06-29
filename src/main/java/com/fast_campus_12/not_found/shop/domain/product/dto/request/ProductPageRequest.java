package com.fast_campus_12.not_found.shop.domain.product.dto.request;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.domain.product.enums.ProductSortBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPageRequest extends PageRequestDto<ProductSortBy> {

    public ProductPageRequest() {
        setPage(1);
        setSortBy(ProductSortBy.PRICE);
        setSort(SortDirection.ASC);
    }
}
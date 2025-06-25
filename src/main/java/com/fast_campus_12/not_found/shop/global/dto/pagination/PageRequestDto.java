package com.fast_campus_12.not_found.shop.global.dto.pagination;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestDto<SORT_BY extends Enum<SORT_BY>> {

    private int page = 1;
    private SORT_BY sortBy;
    private SortDirection sort;

    public int getOffset(int size) {
        return (Math.max(page, 1) - 1) * size;
    }
}

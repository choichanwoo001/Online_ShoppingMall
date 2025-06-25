package com.fast_campus_12.not_found.shop.global.dto.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {
    private List<T> items;
    private int totalCount;
    private int page;
    private int size;
    private boolean hasNext;
    private boolean hasPrev;

    public int getTotalPages() {
        if (size == 0) return 0;
        return (int) Math.ceil((double) totalCount / size);
    }
}
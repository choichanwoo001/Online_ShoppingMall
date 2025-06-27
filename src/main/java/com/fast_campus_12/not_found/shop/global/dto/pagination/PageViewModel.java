package com.fast_campus_12.not_found.shop.global.dto.pagination;

import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class PageViewModel<T> {

    private final PageResponseDto<T> pageResponseDto;

    /**
     * category, subCategory 등 유연하게 확장 가능한 필터 정보
     */
    private final Map<String, String> filters;

    /**
     * 페이징/정렬 링크 생성을 위한 기준 URL
     * ex) /product/category/shirt?sortBy=CREATED_AT&sort=DESC
     */
    private final String baseUrl;

    /**
     * 현재 정렬 기준 필드명
     */
    private final String sortBy;

    /**
     * 현재 정렬 방향 (ASC or DESC)
     */
    private final String sort;

    /**
     * View Fragment path 등 컨텐츠 삽입에 필요한 정보
     * ex) fragments/productList :: productListContent
     */
    private final String contentPath;
}

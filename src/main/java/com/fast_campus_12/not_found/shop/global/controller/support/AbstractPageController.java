package com.fast_campus_12.not_found.shop.global.controller.support;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.response.ProductPageResponse;
import org.springframework.ui.Model;

import java.util.Optional;

public abstract class AbstractPageController<SORT_BY extends Enum<SORT_BY>, SUMMARY_DTO> {

    protected static final int PAGE_SIZE = 6;

    /**
     * 실제 데이터를 조회하는 추상 메서드
     */
    protected abstract PageResponseDto<SUMMARY_DTO> fetchPage(
            String category,
            String subCategory,
            PageRequestDto<SORT_BY> pageRequest
    );

    /**
     * 공통 페이지 모델 생성
     */
    protected ProductPageResponse<SUMMARY_DTO> buildPageModel(
            String category,
            String subCategory,
            String baseUrl,
            PageResponseDto<SUMMARY_DTO> page,
            PageRequestDto<SORT_BY> pageRequest
    ) {
        return ProductPageResponse.<SUMMARY_DTO>builder()
                .pageResponseDto(page)
                .category(category)
                .subCategory(subCategory)
                .baseUrl(baseUrl)
                .sortBy(Optional.ofNullable(pageRequest.getSortBy()).orElse(getDefaultSortBy()).name())
                .sort(Optional.ofNullable(pageRequest.getSort()).orElse(SortDirection.ASC).name())
                .contentPath(getContentPath())
                .build();
    }

    protected abstract SORT_BY getDefaultSortBy();

    protected abstract String getContentPath();

    protected void setModel(Model model, ProductPageResponse<SUMMARY_DTO> page) {
        model.addAttribute("productPage", page);
        model.addAttribute("contentPath", page.getContentPath());
    }
}

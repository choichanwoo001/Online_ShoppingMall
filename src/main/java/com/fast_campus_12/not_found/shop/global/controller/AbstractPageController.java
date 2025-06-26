package com.fast_campus_12.not_found.shop.global.controller;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageViewModel;
import org.springframework.ui.Model;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractPageController<SORT_BY extends Enum<SORT_BY>, DTO> {

    protected static final int PAGE_SIZE = 6;

    protected abstract PageResponseDto<DTO> fetchPage(
            Map<String, String> filters,
            PageRequestDto<SORT_BY> pageRequest
    );

    protected PageViewModel<DTO> buildPageModel(
            Map<String, String> filters,
            String baseUrl,
            PageResponseDto<DTO> page,
            PageRequestDto<SORT_BY> pageRequest
    ) {
        return PageViewModel.<DTO>builder()
                .pageResponseDto(page)
                .filters(filters)
                .baseUrl(baseUrl)
                .sortBy(Optional.ofNullable(pageRequest.getSortBy()).orElse(getDefaultSortBy()).name())
                .sort(Optional.ofNullable(pageRequest.getSort()).orElse(SortDirection.ASC).name())
                .contentPath(getContentPath())
                .build();
    }

    protected abstract SORT_BY getDefaultSortBy();

    protected abstract String getContentPath();

    protected void setModel(Model model, PageViewModel<DTO> page) {
        model.addAttribute("pageView", page);
        model.addAttribute("contentPath", page.getContentPath());
    }
}

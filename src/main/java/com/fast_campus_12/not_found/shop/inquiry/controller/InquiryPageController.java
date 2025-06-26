package com.fast_campus_12.not_found.shop.inquiry.controller;

import com.fast_campus_12.not_found.shop.global.controller.AbstractPageController;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.inquiry.dto.InquiryDto;
import com.fast_campus_12.not_found.shop.inquiry.enums.InquirySortBy;
import com.fast_campus_12.not_found.shop.notice.enums.NoticeSortBy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static com.fast_campus_12.not_found.shop.inquiry.enums.InquirySortBy.CREATED_AT;


@Controller
@RequiredArgsConstructor
public class InquiryPageController extends AbstractPageController<InquirySortBy, InquiryDto> {

    @Override
    protected PageResponseDto<InquiryDto> fetchPage(Map<String, String> filters, PageRequestDto<InquirySortBy> pageRequest) {
        return null;
    }

    @Override
    protected InquirySortBy getDefaultSortBy() {
        return CREATED_AT;
    }

    @Override
    protected String getContentPath() {
        return "notice/noticeList";
    }
}

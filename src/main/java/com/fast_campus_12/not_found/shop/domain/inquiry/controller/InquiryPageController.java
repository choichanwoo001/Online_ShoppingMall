package com.fast_campus_12.not_found.shop.domain.inquiry.controller;

import com.fast_campus_12.not_found.shop.global.controller.AbstractPageController;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageViewModel;
import com.fast_campus_12.not_found.shop.domain.inquiry.dto.InquiryDto;
import com.fast_campus_12.not_found.shop.domain.inquiry.dto.InquiryPageDto;
import com.fast_campus_12.not_found.shop.domain.inquiry.dto.request.InquiryPageRequest;
import com.fast_campus_12.not_found.shop.domain.inquiry.enums.InquirySortBy;
import com.fast_campus_12.not_found.shop.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

import static com.fast_campus_12.not_found.shop.domain.inquiry.enums.InquirySortBy.CREATED_AT;


@Controller
@RequiredArgsConstructor
public class InquiryPageController extends AbstractPageController<InquirySortBy, InquiryDto> {
    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    public String inquiryPage(@ModelAttribute InquiryPageRequest pageRequest, Model model) {

        PageResponseDto<InquiryDto> page
                = fetchPage(null, pageRequest);

        PageViewModel<InquiryDto> response = buildPageModel(null, "/inquiry", page, pageRequest);
        //model.addAttribute("pinnedNotices", inquiryService.findPinnedInquiry());
        setModel(model, response);

        return "layout/base";
    }

    @Override
    protected PageResponseDto<InquiryDto> fetchPage(Map<String, String> filters, PageRequestDto<InquirySortBy> pageRequest) {
        InquiryPageDto inquiry = inquiryService.findUnpinnedInquiries((InquiryPageRequest) pageRequest);

        return PageResponseDto.of(inquiry.getItems(), inquiry.getTotalCount(), pageRequest.getPage(),
                ((InquiryPageRequest) pageRequest).getLimit());
    }

    @Override
    protected InquirySortBy getDefaultSortBy() {
        return CREATED_AT;
    }

    @Override
    protected String getContentPath() {
        return "inquiry/inquiryList";
    }
}

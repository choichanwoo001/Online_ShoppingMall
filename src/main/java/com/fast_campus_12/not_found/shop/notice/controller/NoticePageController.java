package com.fast_campus_12.not_found.shop.notice.controller;

import com.fast_campus_12.not_found.shop.global.controller.AbstractPageController;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageViewModel;
import com.fast_campus_12.not_found.shop.notice.dto.NoticeDto;
import com.fast_campus_12.not_found.shop.notice.dto.NoticePageDto;
import com.fast_campus_12.not_found.shop.notice.dto.request.NoticePageRequest;
import com.fast_campus_12.not_found.shop.notice.enums.NoticeSortBy;
import com.fast_campus_12.not_found.shop.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.fast_campus_12.not_found.shop.notice.enums.NoticeSortBy.CREATED_AT;

@Controller
@RequiredArgsConstructor
public class NoticePageController extends AbstractPageController<NoticeSortBy, NoticeDto>  {
    private final NoticeService noticeService;

    @GetMapping("/notice")
    public String showNotices(@ModelAttribute NoticePageRequest pageRequest,
            Model model) {


        PageResponseDto<NoticeDto> page
                = fetchPage(null, pageRequest);

        PageViewModel<NoticeDto> response = buildPageModel(null, "/notice", page, pageRequest);
        setModel(model, response);
//        model.addAttribute("notices", notices);
        model.addAttribute("pinnedNotices", noticeService.findPinnedNotices());
        model.addAttribute("title", "공지사항");
//        model.addAttribute("subCategory", "Notice");
        return "layout/base";
    }


    @Override
    protected PageResponseDto<NoticeDto> fetchPage(Map<String, String> filters, PageRequestDto<NoticeSortBy> pageRequest) {
        NoticePageDto notices = noticeService.findUnpinnedNotices((NoticePageRequest) pageRequest);

        return PageResponseDto.of(notices.getItems(), notices.getTotalCount(), pageRequest.getPage(),
                ((NoticePageRequest) pageRequest).getLimit());
    }

    @Override
    protected NoticeSortBy getDefaultSortBy() {
        return CREATED_AT;
    }

    @Override
    protected String getContentPath() {
        return "notice/noticeList";
    }

}

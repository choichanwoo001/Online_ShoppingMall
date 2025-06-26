package com.fast_campus_12.not_found.shop.notice.controller;

import com.fast_campus_12.not_found.shop.notice.dto.NoticeDto;
import com.fast_campus_12.not_found.shop.notice.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String showNotices(Model model) {
        List<NoticeDto> notices = noticeService.findAllNotices();
        model.addAttribute("notices", notices);
        model.addAttribute("title", "공지사항");
        model.addAttribute("subCategory", "Notice");
        model.addAttribute("contentPath", "notice/noticeList");
        return "layout/base";
    }
}

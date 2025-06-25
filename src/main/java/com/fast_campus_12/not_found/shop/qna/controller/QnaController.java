package com.fast_campus_12.not_found.shop.qna.controller;

import com.fast_campus_12.not_found.shop.qna.dto.QuestionDto;
import com.fast_campus_12.not_found.shop.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    // ✅ 1. 기본 요청: /qna → /qna/1 리다이렉트
    @GetMapping
    public String redirectToFirstPage() {
        return "redirect:/qna/1";
    }

    // ✅ 2. 페이지별 요청 처리: /qna/1, /qna/2, ...
    @GetMapping("/{page}")
    public String qnaPage(@PathVariable("page") int page, Model model) {
        List<QuestionDto> all = qnaService.getAllQuestions();
        int pageSize = 10;
        int total = all.size();

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<QuestionDto> pageList = all.subList(fromIndex, toIndex);

        model.addAttribute("questions", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) total / pageSize));
        model.addAttribute("contentPath", "qna/list");

        return "layout/base";
    }
}

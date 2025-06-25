package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.service.UserService;
import com.fast_campus_12.not_found.shop.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    // 쿠폰 페이지 이동
    @GetMapping("/coupon/{pageName}")
    public String findIdPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("title", "쿠폰");
        model.addAttribute("contentPath", "myshop/" + pageName);
        return "layout/base";
    }
}
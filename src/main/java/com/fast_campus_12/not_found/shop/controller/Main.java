package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.MainBannerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class Main {

    @GetMapping("/main")
    public String orderRenderPage(Model model) {
        model.addAttribute("contentPath", "main");
        List<MainBannerDto> bigBanners = List.of(
                new MainBannerDto("index3.jpg", "/event/1"), // image: 사진링크 path: href링크
                new MainBannerDto("index7.jpg", "/event/2"));
        model.addAttribute("bigBanners", bigBanners);
        return "layout/base";


    }
}

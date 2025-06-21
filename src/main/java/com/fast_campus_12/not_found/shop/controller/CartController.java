package com.fast_campus_12.not_found.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {
    @GetMapping("/order/{pageName}")
    public String renderPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("contentPath", "order/" + pageName);
        return "layout/base";
    }
    @GetMapping("/order/cart")
    public String showPopup(Model model) {
        model.addAttribute("contentPath", "order/cart");
        // 예시: 동적으로 팝업 데이터 생성 또는 DB에서 조회
        String dynamicTitle = "새로운 소식!";
        String dynamicMessage = "<p>오늘의 <b>특별 할인</b> 이벤트를 놓치지 마세요!</p>";
        boolean showPopupCloseButton = true;

        // Model에 팝업 데이터를 담아서 Thymeleaf로 전달
        // (이 방식은 팝업 내용을 페이지 컨트롤러에서 직접 제어할 때 사용)
        model.addAttribute("popupTitle", dynamicTitle);
        model.addAttribute("popupMessage", dynamicMessage);
        model.addAttribute("showCloseButton", showPopupCloseButton);
        return "layout/base";
    }

    @GetMapping("/myshop/{pageName}")
    public String renderMyShop(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("contentPath", "myshop/" + pageName);
        return "layout/base";
    }
}




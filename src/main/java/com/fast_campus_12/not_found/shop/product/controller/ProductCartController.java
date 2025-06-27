package com.fast_campus_12.not_found.shop.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
@Slf4j
public class ProductCartController {
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam String title,
            @RequestParam String color,
            @RequestParam String size,
            @RequestParam int quantity
    ) {
        // 이 값들로 장바구니 처리
        return "redirect:/cart";
    }
}

package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductOptionPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-option-price")
class ProductOptionPriceViewController {
    private final ProductOptionPriceService productOptionPriceService;

    @GetMapping("/form")
    public String createForm(Model model) {
        return "page/product/option-price-form";
    }
}

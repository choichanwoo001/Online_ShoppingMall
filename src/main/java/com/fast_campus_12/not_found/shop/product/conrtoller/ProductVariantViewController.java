package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-variant")
class ProductVariantViewController {
    private final ProductVariantService productVariantService;

    @GetMapping("/list")
    public String showVariants(Model model) {
        return "page/product/variant-list";
    }
}

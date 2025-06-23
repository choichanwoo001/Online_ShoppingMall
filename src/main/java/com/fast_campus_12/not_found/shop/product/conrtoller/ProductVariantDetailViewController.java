package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductVariantDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-variant-detail")
class ProductVariantDetailViewController {
    private final ProductVariantDetailService productVariantDetailService;

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return "page/product/variant-detail";
    }
}

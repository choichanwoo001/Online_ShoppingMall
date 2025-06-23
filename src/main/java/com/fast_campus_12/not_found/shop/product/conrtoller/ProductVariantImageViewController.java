package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductVariantImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-variant-image")
class ProductVariantImageViewController {
    private final ProductVariantImageService productVariantImageService;

    @GetMapping("/form")
    public String uploadForm(Model model) {
        return "page/product/variant-image-form";
    }
}

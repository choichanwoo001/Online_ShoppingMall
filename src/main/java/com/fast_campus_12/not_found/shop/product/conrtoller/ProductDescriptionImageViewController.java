package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductDescriptionImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-description-image")
class ProductDescriptionImageViewController {
    private final ProductDescriptionImageService productDescriptionImageService;

    @GetMapping("/{id}")
    public String viewImage(@PathVariable Long id, Model model) {
        // TODO: 서비스 메서드 필요
        return "page/product/description-image";
    }
}

package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductVariantDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-variant-details")
@RequiredArgsConstructor
public class ProductVariantDetailController {
    private final ProductVariantDetailService productVariantDetailService;

    @PostMapping
    public String create() {
        return "create product variant detail";
    }
}

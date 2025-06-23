package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductDescriptionImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-description-images")
@RequiredArgsConstructor
public class ProductDescriptionImageController {
    private final ProductDescriptionImageService productDescriptionImageService;

    @GetMapping
    public String getAll() {
        return "get all product description images";
    }
}

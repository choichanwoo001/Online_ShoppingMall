package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ProductOptionPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-option-prices")
@RequiredArgsConstructor
public class ProductOptionPriceController {
    private final ProductOptionPriceService productOptionPriceService;

    @PostMapping
    public String create() {
        return "create product option price";
    }
}

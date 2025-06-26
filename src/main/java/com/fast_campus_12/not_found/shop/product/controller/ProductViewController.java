package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
}

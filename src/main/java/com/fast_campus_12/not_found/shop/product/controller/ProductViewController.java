package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;


    @GetMapping("/{id}")
    public String productDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("contentPath", "product/productDetail");
        model.addAttribute("product", productService.getProductDetailDto(new BigInteger(id)));
        return "layout/base";
    }

}

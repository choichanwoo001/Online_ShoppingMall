package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    // 카테고리 목록 (ex: /product/category/top)
    @GetMapping("/category/{category}")
    public String productByCategory(@PathVariable("category") String category, Model model) {
        List<ProductSummaryDto> productList = productService.getSummaryByCategory(category);

        model.addAttribute("title", category.toUpperCase());
        model.addAttribute("productList", productList);
        model.addAttribute("category", category);
        model.addAttribute("productCount", productList.size());
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }

    // 카테고리 목록 (ex: /product/category/top/knit)
    @GetMapping("/category/{category}/{subCategory}")
    public String productBySubCategory(@PathVariable("category") String category,
                                       @PathVariable("subCategory") String subCategory,
                                       Model model) {
        List<ProductSummaryDto> productList = productService.getSummaryByCategory(category);

        model.addAttribute("title", category.toUpperCase());
        model.addAttribute("productList", productList);
        model.addAttribute("category", category);
        model.addAttribute("productCount", productList.size());
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }
}

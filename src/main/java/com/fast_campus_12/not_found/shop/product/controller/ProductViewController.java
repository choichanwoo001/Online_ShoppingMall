package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
    private final int pageSize = 6;

    @GetMapping("/category/{category}")
    public String productByCategory(@PathVariable("category") String category,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    Model model) {
        return handleProductList(category, null, page, model);
    }

    @GetMapping("/category/{category}/{subCategory}")
    public String productBySubCategory(@PathVariable("category") String category,
                                       @PathVariable("subCategory") String subCategory,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       Model model) {
        return handleProductList(category, subCategory, page, model);
    }

    private String handleProductList(String category, String subCategory, int page, Model model) {
        int offset = (page - 1) * pageSize;

        ProductPageDto productPageDto =
                productService.getSummaryByCategory(category, subCategory, offset, pageSize);

        PageResponseDto<ProductSummaryDto> pageResponseDto = PageResponseDto.<ProductSummaryDto>builder()
                .items(productPageDto.getItems())
                .totalCount(productPageDto.getTotalCount())
                .page(page)
                .size(pageSize)
                .hasNext(productPageDto.getTotalCount() > page * pageSize)
                .hasPrev(page > 1)
                .build();

        model.addAttribute("pageResponseDto", pageResponseDto);
        model.addAttribute("title", category.toUpperCase());
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory); // null 가능
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }
}

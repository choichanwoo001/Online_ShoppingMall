package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.request.ProductPageRequest;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import com.fast_campus_12.not_found.shop.product.enums.SpecialProductCategory;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductViewController {

    private final ProductService productService;
    private static final int PAGE_SIZE = 6;

    @GetMapping("/special/{special}")
    public String specialProduct(@PathVariable("special") SpecialProductCategory special,
                                 @ModelAttribute ProductPageRequest pageRequest,
                                 Model model) {

        ProductSpecialSummaryRequestDto requestDto = ProductSpecialSummaryRequestDto.builder()
                .specialProductCategory(special)
                .limit(PAGE_SIZE)
                .offset(pageRequest.getOffset(PAGE_SIZE))
                .sortBy(pageRequest.getSortBy())
                .sortDirection(pageRequest.getSort())
                .build();

        ProductPageDto result = productService.getSummaryBySpecialCategory(requestDto);

        PageResponseDto<ProductSummaryDto> pageResponse = PageResponseDto.of(
                result.getItems(),
                result.getTotalCount(),
                pageRequest.getPage(),
                PAGE_SIZE
        );

        model.addAttribute("pageResponseDto", pageResponse);
        model.addAttribute("baseUrl", "/product/special/" + special.name());
        model.addAttribute("sortBy", pageRequest.getSortBy().name());
        model.addAttribute("sort", pageRequest.getSort());
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }

    @GetMapping("/category/{category}")
    public String productByCategory(@PathVariable String category,
                                    @ModelAttribute ProductPageRequest pageRequest,
                                    Model model) {
        return renderCategoryList(category, null, pageRequest, model);
    }

    @GetMapping("/category/{category}/{subCategory}")
    public String productBySubCategory(@PathVariable String category,
                                       @PathVariable String subCategory,
                                       @ModelAttribute ProductPageRequest pageRequest,
                                       Model model) {
        return renderCategoryList(category, subCategory, pageRequest, model);
    }

    private String renderCategoryList(String category, String subCategory,
                                      ProductPageRequest pageRequest, Model model) {

        ProductSummaryRequestDto dto = ProductSummaryRequestDto.builder()
                .category(category)
                .subCategory(subCategory)
                .limit(PAGE_SIZE)
                .offset(pageRequest.getOffset(PAGE_SIZE))
                .sortBy(pageRequest.getSortBy())
                .sortDirection(pageRequest.getSort())
                .build();

        ProductPageDto result = productService.getSummaryByCategory(dto);

        PageResponseDto<ProductSummaryDto> pageResponse = PageResponseDto.of(
                result.getItems(),
                result.getTotalCount(),
                pageRequest.getPage(),
                PAGE_SIZE
        );

        String baseUrl = (subCategory == null)
                ? "/product/category/" + category
                : "/product/category/" + category + "/" + subCategory;

        model.addAttribute("pageResponseDto", pageResponse);
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("sortBy", pageRequest.getSortBy().name());
        model.addAttribute("sort", pageRequest.getSort());
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }
}


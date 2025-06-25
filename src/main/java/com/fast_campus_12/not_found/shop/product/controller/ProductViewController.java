package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.request.ProductPageRequest;
import com.fast_campus_12.not_found.shop.product.dto.response.ProductPageResponse;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import com.fast_campus_12.not_found.shop.product.enums.SpecialProductCategory;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

        ProductPageResponse response = ProductPageResponse.builder()
                .pageResponseDto(pageResponse)
                .category(null)
                .subCategory(null)
                .baseUrl("/product/special/" + special.name())
                .sortBy(Optional.ofNullable(pageRequest.getSortBy()).orElse(ProductSortBy.PRICE).name())
                .sort(Optional.ofNullable(pageRequest.getSort()).orElse(SortDirection.ASC).name())
                .contentPath("product/productList")
                .build();

        model.addAttribute("productPage", response);
        model.addAttribute("contentPath", response.getContentPath());
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

        ProductPageResponse response = ProductPageResponse.builder()
                .pageResponseDto(pageResponse)
                .category(category)
                .subCategory(subCategory)
                .baseUrl(baseUrl)
                .sortBy(Optional.ofNullable(pageRequest.getSortBy()).orElse(ProductSortBy.PRICE).name())
                .sort(Optional.ofNullable(pageRequest.getSort()).orElse(SortDirection.ASC).name())
                .contentPath("product/productList")
                .build();

        model.addAttribute("productPage", response);
        model.addAttribute("contentPath", response.getContentPath());
        return "layout/base";
    }
}

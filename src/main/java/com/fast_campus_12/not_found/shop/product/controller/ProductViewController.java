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

import java.util.Objects;
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

        String baseUrl = "/product/special/" + special.name();

        ProductPageResponse response = buildProductPageResponse(
                pageResponse, null, null, baseUrl, pageRequest
        );

        setModel(model, response);
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

        String baseUrl = subCategory == null
                ? "/product/category/" + category
                : "/product/category/" + category + "/" + subCategory;

        ProductPageResponse response = buildProductPageResponse(
                pageResponse, category, subCategory, baseUrl, pageRequest
        );

        setModel(model, response);
        return "layout/base";
    }

    private ProductPageResponse buildProductPageResponse(
            PageResponseDto<ProductSummaryDto> pageResponse,
            String category,
            String subCategory,
            String baseUrl,
            ProductPageRequest pageRequest
    ) {
        return ProductPageResponse.builder()
                .pageResponseDto(pageResponse)
                .category(category)
                .subCategory(subCategory)
                .baseUrl(baseUrl)
                .sortBy(resolveSortBy(pageRequest))
                .sort(resolveSort(pageRequest))
                .contentPath("product/productList")
                .build();
    }

    private String resolveSortBy(ProductPageRequest pageRequest) {
        return Optional.ofNullable(pageRequest.getSortBy())
                .orElse(ProductSortBy.PRICE)
                .name();
    }

    private String resolveSort(ProductPageRequest pageRequest) {
        return Optional.ofNullable(pageRequest.getSort())
                .orElse(SortDirection.ASC)
                .name();
    }

    private void setModel(Model model, ProductPageResponse response) {
        model.addAttribute("productPage", response);
        model.addAttribute("contentPath", response.getContentPath());
    }
}

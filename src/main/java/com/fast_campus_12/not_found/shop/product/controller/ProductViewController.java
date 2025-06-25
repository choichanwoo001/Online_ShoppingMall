package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import com.fast_campus_12.not_found.shop.product.enums.SpecialProductCategory;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductViewController {

    private final ProductService productService;
    private static final int PAGE_SIZE = 6;

    @GetMapping("/special/{special}")
    public String specialProduct(@PathVariable("special") SpecialProductCategory special,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "sortBy", defaultValue = "PRICE") ProductSortBy sortBy,
                                 @RequestParam(value = "sort", defaultValue = "ASC") SortDirection sort,
                                 Model model) {

        ProductSpecialSummaryRequestDto requestDto = ProductSpecialSummaryRequestDto.builder()
                .specialProductCategory(special)
                .limit(PAGE_SIZE)
                .offset(getOffset(page))
                .sortBy(sortBy)
                .sortDirection(sort)
                .build();

        ProductPageDto result = productService.getSummaryBySpecialCategory(requestDto);
        PageResponseDto<ProductSummaryDto> pageResponse = buildPageResponse(result, page);

        model.addAttribute("pageResponseDto", pageResponse);
        model.addAttribute("baseUrl", "/product/special/" + special.name());
        model.addAttribute("sortBy", sortBy.name());
        model.addAttribute("sort", sort);
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }

    @GetMapping("/category/{category}")
    public String productByCategory(@PathVariable("category") String category,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "sortBy", defaultValue = "PRICE") ProductSortBy sortBy,
                                    @RequestParam(value = "sort", defaultValue = "ASC") SortDirection sort,
                                    Model model) {

        return renderCategoryList(category, null, page, sortBy, sort, model);
    }

    @GetMapping("/category/{category}/{subCategory}")
    public String productBySubCategory(@PathVariable("category") String category,
                                       @PathVariable("subCategory") String subCategory,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "sortBy", defaultValue = "PRICE") ProductSortBy sortBy,
                                       @RequestParam(value = "sort", defaultValue = "ASC") SortDirection sort,
                                       Model model) {

        return renderCategoryList(category, subCategory, page, sortBy, sort, model);
    }

    private String renderCategoryList(String category, String subCategory, int page,
                                      ProductSortBy sortBy, SortDirection sort, Model model) {

        ProductSummaryRequestDto dto = ProductSummaryRequestDto.builder()
                .category(category)
                .subCategory(subCategory)
                .limit(PAGE_SIZE)
                .offset(getOffset(page))
                .sortBy(sortBy)
                .sortDirection(sort)
                .build();

        ProductPageDto result = productService.getSummaryByCategory(dto);
        PageResponseDto<ProductSummaryDto> pageResponse = buildPageResponse(result, page);

        String baseUrl = (subCategory == null)
                ? "/product/category/" + category
                : "/product/category/" + category + "/" + subCategory;

        model.addAttribute("pageResponseDto", pageResponse);
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("sortBy", sortBy.name());
        model.addAttribute("sort", sort);
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }

    private int getOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    private PageResponseDto<ProductSummaryDto> buildPageResponse(ProductPageDto dto, int page) {
        int total = dto.getTotalCount();
        return PageResponseDto.<ProductSummaryDto>builder()
                .items(dto.getItems())
                .totalCount(total)
                .page(page)
                .size(PAGE_SIZE)
                .hasNext(total > page * PAGE_SIZE)
                .hasPrev(page > 1)
                .build();
    }
}


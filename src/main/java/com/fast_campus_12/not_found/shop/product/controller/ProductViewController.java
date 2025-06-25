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

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductViewController {

    private final ProductService productService;
    private final int pageSize = 6;

    @GetMapping("/special/{special}")
    public String specialProduct(@PathVariable("special") SpecialProductCategory special,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "sortBy", required = false, defaultValue = "PRICE") ProductSortBy sortBy,
                                    @RequestParam(value = "sort", required = false, defaultValue = "ASC") SortDirection sort,
                                    Model model) {

        int offset = (page - 1) * pageSize;

        ProductSpecialSummaryRequestDto specialSummaryRequestDto = ProductSpecialSummaryRequestDto.builder()
                .specialProductCategory(special)
                .limit(pageSize)
                .offset(offset)
                .sortDirection(sort)
                .sortBy(sortBy)
                .build();

        ProductPageDto productPageDto =
                productService.getSummaryBySpecialCategory(specialSummaryRequestDto);

        PageResponseDto<ProductSummaryDto> pageResponseDto = PageResponseDto.<ProductSummaryDto>builder()
                .items(productPageDto.getItems())
                .totalCount(productPageDto.getTotalCount())
                .page(page)
                .size(pageSize)
                .hasNext(productPageDto.getTotalCount() > page * pageSize)
                .hasPrev(page > 1)
                .build();

        log.error("{}", pageResponseDto.getItems());
        model.addAttribute("pageResponseDto", pageResponseDto);
//        model.addAttribute("category", category);
//        model.addAttribute("subCategory", subCategory); // null 가능
        model.addAttribute("baseUrl", "/product/special/" + special.name());
        model.addAttribute("sortBy", sortBy.name());
        model.addAttribute("sort", sort); // ASC, DESC
        model.addAttribute("contentPath", "product/productList");


        return "layout/base";
    }

    @GetMapping("/category/{category}")
    public String productByCategory(@PathVariable("category") String category,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "sortBy", required = false, defaultValue = "PRICE") ProductSortBy sortBy,
                                    @RequestParam(value = "sort", required = false, defaultValue = "ASC") SortDirection sort,
                                    Model model) {

        log.error("{}, {}",sortBy, sort);
        model.addAttribute("baseUrl", "/product/category/" + category);
        return handleProductList(category, null, page, model, sort, sortBy);
    }

    @GetMapping("/category/{category}/{subCategory}")
    public String productBySubCategory(@PathVariable("category") String category,
                                       @PathVariable("subCategory") String subCategory,
                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "sortBy", required = false, defaultValue = "PRICE") ProductSortBy sortBy,
                                       @RequestParam(value = "sort", required = false, defaultValue = "ASC") SortDirection sort,
                                       Model model) {
        model.addAttribute("baseUrl", "/product/category/" + category + "/" + subCategory);
        return handleProductList(category, subCategory, page, model, sort, sortBy);
    }

    private String handleProductList(String category, String subCategory, int page, Model model, SortDirection sort, ProductSortBy sortBy) {
        int offset = (page - 1) * pageSize;

        ProductSummaryRequestDto dto = ProductSummaryRequestDto.builder()
                .category(category)
                .subCategory(subCategory)
                .limit(pageSize)
                .offset(offset)
                .sortDirection(sort)
                .sortBy(sortBy)
                .build();

        ProductPageDto productPageDto =
                productService.getSummaryByCategory(dto);

        PageResponseDto<ProductSummaryDto> pageResponseDto = PageResponseDto.<ProductSummaryDto>builder()
                .items(productPageDto.getItems())
                .totalCount(productPageDto.getTotalCount())
                .page(page)
                .size(pageSize)
                .hasNext(productPageDto.getTotalCount() > page * pageSize)
                .hasPrev(page > 1)
                .build();

        model.addAttribute("pageResponseDto", pageResponseDto);
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory); // null 가능
        model.addAttribute("sortBy", dto.getSortBy().name()); // enum일 경우 .name() 붙이기
        model.addAttribute("sort", dto.getSortDirection()); // ASC, DESC
        model.addAttribute("contentPath", "product/productList");

        return "layout/base";
    }
}

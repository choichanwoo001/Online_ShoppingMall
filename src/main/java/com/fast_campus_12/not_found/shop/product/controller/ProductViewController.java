package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.global.controller.support.AbstractPageController;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController extends AbstractPageController<ProductSortBy, ProductSummaryDto> {

    private final ProductService productService;

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
        PageResponseDto<ProductSummaryDto> page = PageResponseDto.of(result.getItems(), result.getTotalCount(), pageRequest.getPage(), PAGE_SIZE);

        ProductPageResponse<ProductSummaryDto> response = buildPageModel(null, null, "/product/special/" + special.name(), page, pageRequest);
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
                                      PageRequestDto<ProductSortBy> pageRequest, Model model) {

        ProductSummaryRequestDto dto = ProductSummaryRequestDto.builder()
                .category(category)
                .subCategory(subCategory)
                .limit(PAGE_SIZE)
                .offset(pageRequest.getOffset(PAGE_SIZE))
                .sortBy(pageRequest.getSortBy())
                .sortDirection(pageRequest.getSort())
                .build();

        ProductPageDto result = productService.getSummaryByCategory(dto);
        PageResponseDto<ProductSummaryDto> page = PageResponseDto.of(result.getItems(), result.getTotalCount(), pageRequest.getPage(), PAGE_SIZE);

        String baseUrl = Objects.isNull(subCategory)
                ? "/product/category/" + category
                : "/product/category/" + category + "/" + subCategory;

        ProductPageResponse<ProductSummaryDto> response = buildPageModel(category, subCategory, baseUrl, page, pageRequest);
        setModel(model, response);
        return "layout/base";
    }

    @Override
    protected PageResponseDto<ProductSummaryDto> fetchPage(String category, String subCategory, PageRequestDto<ProductSortBy> pageRequest) {
        ProductSummaryRequestDto dto = ProductSummaryRequestDto.builder()
                .category(category)
                .subCategory(subCategory)
                .limit(PAGE_SIZE)
                .offset(pageRequest.getOffset(PAGE_SIZE))
                .sortBy(pageRequest.getSortBy())
                .sortDirection(pageRequest.getSort())
                .build();

        ProductPageDto result = productService.getSummaryByCategory(dto);

        return PageResponseDto.of(
                result.getItems(),
                result.getTotalCount(),
                pageRequest.getPage(),
                PAGE_SIZE
        );
    }

    @Override
    protected ProductSortBy getDefaultSortBy() {
        return ProductSortBy.PRICE;
    }

    @Override
    protected String getContentPath() {
        return "product/productList";
    }
}

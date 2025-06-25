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

/**
 * 상품 목록 및 특가 상품 관련 요청을 처리하는 View Controller
 *
 * <p>다음 경로에 대한 요청 처리:
 * <ul>
 *   <li>/product/category/{category}</li>
 *   <li>/product/category/{category}/{subCategory}</li>
 *   <li>/product/special/{special}</li>
 * </ul>
 *
 * @author : jys01012@gmail.com
 * @version : 1.0
 */
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController extends AbstractPageController<ProductSortBy, ProductSummaryDto> {

    private final ProductService productService;

    /**
     * 중분류(Lv2) 카테고리 기반 상품 목록 요청 처리
     *
     * @param category Lv2 카테고리 명
     * @param pageRequest 페이지 요청 정보
     * @param model 뷰 렌더링을 위한 모델
     * @return base 레이아웃 경로
     */
    @GetMapping("/category/{category}")
    public String productByCategory(@PathVariable String category,
                                    @ModelAttribute ProductPageRequest pageRequest,
                                    Model model) {
        return renderCategoryList(category, null, pageRequest, model);
    }

    /**
     * 중분류(Lv2), 소분류(Lv3) 카테고리 기반 상품 목록 요청 처리
     *
     * @param category Lv2 카테고리 명
     * @param subCategory Lv3 카테고리 명
     * @param pageRequest 페이지 요청 정보
     * @param model 타임리프 렌더를 위한 모델
     * @return base 레이아웃 경로
     */
    @GetMapping("/category/{category}/{subCategory}")
    public String productBySubCategory(@PathVariable String category,
                                       @PathVariable String subCategory,
                                       @ModelAttribute ProductPageRequest pageRequest,
                                       Model model) {
        return renderCategoryList(category, subCategory, pageRequest, model);
    }

    /**
     * Best, 신상품 목록 요청 처리
     *
     * @param special 특가 카테고리
     * @param pageRequest 페이지 요청 정보
     * @param model 타임리프 렌더를 위한 모델
     * @return base 레이아웃 경로
     */
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

    /**
     * 카테고리 기반 상품 목록 조회 및 렌더링 처리 (내부 메서드)
     */
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

    /**
     * 추상 컨트롤러 구현: 상품 목록 조회 처리
     */
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

    /**
     * 기본 정렬 기준 반환
     */
    @Override
    protected ProductSortBy getDefaultSortBy() {
        return ProductSortBy.PRICE;
    }

    /**
     * 콘텐츠 Fragment 경로 반환
     */
    @Override
    protected String getContentPath() {
        return "product/productList";
    }
}

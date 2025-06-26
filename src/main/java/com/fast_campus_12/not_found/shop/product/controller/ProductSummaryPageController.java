package com.fast_campus_12.not_found.shop.product.controller;

import com.fast_campus_12.not_found.shop.global.controller.AbstractPageController;
import com.fast_campus_12.not_found.shop.global.dto.http.PageResponseDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageViewModel;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.request.ProductPageRequest;
import com.fast_campus_12.not_found.shop.product.enums.ProductSortBy;
import com.fast_campus_12.not_found.shop.product.enums.SpecialProductCategory;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 상품 목록 및 특가 상품 관련 요청을 처리하는 Controller
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
public class ProductSummaryPageController extends AbstractPageController<ProductSortBy, ProductSummaryDto> {
    protected static final int PAGE_SIZE = 6;
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

        // 필터를 Map으로 대체
        Map<String, String> filters = Map.of("special", special.name());
        PageViewModel<ProductSummaryDto> response = buildPageModel(filters, "/product/special/" + special.name(), page, pageRequest);
        setModel(model, response);

        return "layout/base";
    }

    /**
     * 카테고리 기반 상품 목록 조회 및 렌더링 처리 (내부 메서드)
     */
    private String renderCategoryList(String category, String subCategory,
                                      PageRequestDto<ProductSortBy> pageRequest, Model model) {

        Map<String, String> filters = new HashMap<>();
        filters.put("category", category);
        if (Objects.nonNull(subCategory)) filters.put("subCategory", subCategory);

        PageResponseDto<ProductSummaryDto> page = fetchPage(filters, pageRequest);

        String baseUrl = Objects.isNull(subCategory)
                ? "/product/category/" + category
                : "/product/category/" + category + "/" + subCategory;

        PageViewModel<ProductSummaryDto> response = buildPageModel(filters, baseUrl, page, pageRequest);
        setModel(model, response);

        return "layout/base";
    }

    @Override
    protected PageResponseDto<ProductSummaryDto> fetchPage(Map<String, String> filters, PageRequestDto<ProductSortBy> pageRequest) {
        ProductSummaryRequestDto dto = ProductSummaryRequestDto.builder()
                .category(filters.get("category"))
                .subCategory(filters.get("subCategory"))
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

    @Override
    protected String getContentPath() {
        return "product/productList";
    }
}

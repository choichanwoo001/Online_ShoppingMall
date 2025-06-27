package com.fast_campus_12.not_found.shop.domain.product.cache;

import com.fast_campus_12.not_found.shop.domain.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.domain.product.dto.SubCategoryDto;
import com.fast_campus_12.not_found.shop.domain.product.service.CategoryService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryCache {

    private final CategoryService categoryService;
    @Getter
    private List<CategoryMenuDto> cachedCategoryMenus;
    @Getter
    private Map<String, List<SubCategoryDto>> cachedSubCategoryMenus;


    @PostConstruct
    public void init() {
        cachedCategoryMenus = categoryService.getCategoryMenus();
        cachedSubCategoryMenus = categoryService.getSubCategoryMenus();
        log.info("Success to init category menus cache");
    }
}

package com.fast_campus_12.not_found.shop.product.cache;

import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.service.CategoryService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryCache {

    private final CategoryService categoryService;
    @Getter
    private List<CategoryMenuDto> cachedCategoryMenus;

    @PostConstruct
    public void init() {
        cachedCategoryMenus = categoryService.getCategoryMenus();
        log.info("Success to init category menus cache");
    }
}

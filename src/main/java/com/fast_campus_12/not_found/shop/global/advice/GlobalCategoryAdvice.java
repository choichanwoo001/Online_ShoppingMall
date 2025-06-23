package com.fast_campus_12.not_found.shop.global.advice;

import com.fast_campus_12.not_found.shop.product.cache.CategoryCache;
import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalCategoryAdvice {

    private final CategoryCache categoryCache;

    @ModelAttribute("categoryMenuList")
    public List<CategoryMenuDto> categoryTree() {
        return categoryCache.getCachedCategoryMenus();
    }
}

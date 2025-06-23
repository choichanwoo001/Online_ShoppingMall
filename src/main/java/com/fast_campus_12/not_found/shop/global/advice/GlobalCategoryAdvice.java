package com.fast_campus_12.not_found.shop.global.advice;

import com.fast_campus_12.not_found.shop.product.cache.CategoryCache;
import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.dto.SubCategoryDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalCategoryAdvice {

    private final CategoryCache categoryCache;

    @ModelAttribute("categoryMenuList")
    public List<CategoryMenuDto> categoryTree() {
        return categoryCache.getCachedCategoryMenus();
    }

    @ModelAttribute("subCategoryMenuList")
    public List<SubCategoryDto> subCategoryTree(HttpServletRequest request) {
        Map<String, List<SubCategoryDto>> subCategoryMenus  = categoryCache.getCachedSubCategoryMenus();
        log.error("{}", subCategoryMenus);
        log.error("{}, {}", request.getRequestURI(), request.getMethod());
        String categoryName = categoryParser(request.getRequestURI());

        if(Objects.isNull(categoryName)) return null;

        if(!subCategoryMenus.containsKey(categoryName)) return null;

        return subCategoryMenus.get(categoryName);

    }

    private String categoryParser(String uri) {
        String[] segments = uri.split("/");

        if (segments.length >= 4 && segments[1].equals("product") && segments[2].equals("category")) {
           return segments[3];
        }
        return null;
    }
}

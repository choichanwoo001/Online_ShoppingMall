package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.dto.SubCategoryDto;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryMenuDto> getCategoryMenus();
    Map<String, List<SubCategoryDto>> getSubCategoryMenus();
}

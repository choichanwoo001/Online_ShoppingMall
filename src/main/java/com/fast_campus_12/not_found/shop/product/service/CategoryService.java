package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.model.Lv1Category;

import java.util.List;

public interface CategoryService {
    List<CategoryMenuDto> getCategoryMenus();
}

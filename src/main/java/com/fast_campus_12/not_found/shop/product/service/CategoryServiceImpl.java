package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.dto.SubCategoryDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<CategoryMenuDto> getCategoryMenus() {
        // TODO: 임시 구현. 추후 실제 데이터로 변경
        return Collections.emptyList();
    }

    @Override
    public Map<String, List<SubCategoryDto>> getSubCategoryMenus() {
        // TODO: 임시 구현. 추후 실제 데이터로 변경
        return Collections.emptyMap();
    }
}

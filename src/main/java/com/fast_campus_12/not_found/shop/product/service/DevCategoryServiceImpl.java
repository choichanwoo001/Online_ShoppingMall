package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.mapper.CategoryMapper;
import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.dto.FlatSubCategoryDto;
import com.fast_campus_12.not_found.shop.product.dto.SubCategoryDto;
import com.fast_campus_12.not_found.shop.product.model.Lv1Category;
import com.fast_campus_12.not_found.shop.product.model.Lv2Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DevCategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public List<CategoryMenuDto> getCategoryMenus() {
        List<Lv1Category> categories = categoryMapper.getLv1CategoriesWithLv2();

        List<CategoryMenuDto> dynamicMenus = toCategoryMenuDtoList(categories);
        List<CategoryMenuDto> fixedMenus = getPredefinedMenus();

        return Stream.concat(fixedMenus.stream(), dynamicMenus.stream())
                .sorted(Comparator.comparingInt(CategoryMenuDto::getPriority))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<SubCategoryDto>> getSubCategoryMenus() {
        List<FlatSubCategoryDto> flat = categoryMapper.getAllSubCategoriesGroupedByLv2();

        return flat.stream()
                .collect(Collectors.groupingBy(
                        FlatSubCategoryDto::getLv2Name,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                row -> SubCategoryDto.builder()
                                        .categoryName(row.getCategoryName())
                                        .link(row.getLink())
                                        .build(),
                                Collectors.toList()
                        )
                ));
    }

    private List<CategoryMenuDto> getPredefinedMenus() {
        return List.of(
                CategoryMenuDto.builder().categoryName("BEST").categoryLink("/product/category/best").priority(-99).build(),
                CategoryMenuDto.builder().categoryName("NEW").categoryLink("/product/category/new").priority(-98).build()
        );
    }


    public List<CategoryMenuDto> toCategoryMenuDtoList(List<Lv1Category> lv1Categories) {
        return lv1Categories.stream()
                .flatMap(lv1 -> lv1.getChildren().stream())  // Lv2 리스트 펼치기
                .filter(lv2 -> "Y".equals(lv2.getIsActive()))
                .sorted(Comparator.comparingInt(Lv2Category::getSortOrder))
                .map(lv2 -> CategoryMenuDto.builder()
                        .categoryName(lv2.getName().toUpperCase())  // OUTER, TOP ...
                        .categoryLink("/product/category/" + lv2.getName().toLowerCase())
                        .priority(lv2.getSortOrder())
                        .build())
                .collect(Collectors.toList());
    }
}

package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.mapper.CategoryMapper;
import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.model.Lv1Category;
import com.fast_campus_12.not_found.shop.product.model.Lv2Category;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@lombok.extern.slf4j.Slf4j
@Service
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DevCategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public List<CategoryMenuDto> getCategoryMenus() {
        log.info("Generating category menus");
        long start = System.currentTimeMillis();
        List<Lv1Category> categories = categoryMapper.getLv1CategoriesWithLv2();

        List<CategoryMenuDto> dynamicMenus = toCategoryMenuDtoList(categories);
        List<CategoryMenuDto> fixedMenus = getPredefinedMenus();


        long end = System.currentTimeMillis();
        log.info("Success to generate category menus in {} ms", end - start);

        return Stream.concat(fixedMenus.stream(), dynamicMenus.stream())
                .sorted(Comparator.comparingInt(CategoryMenuDto::getPriority))
                .collect(Collectors.toList());
    }

//    private List<CategoryMenuDto> generateDummyCategoryMenus() {
//        return List.of(
//                CategoryMenuDto.builder().categoryName("BEST").categoryLink("/product/category/best").priority(0).build(),
//                CategoryMenuDto.builder().categoryName("NEW").categoryLink("/product/category/new").priority(1).build(),
//                CategoryMenuDto.builder().categoryName("OUTER").categoryLink("/product/category/outer").priority(2).build(),
//                CategoryMenuDto.builder().categoryName("TOP").categoryLink("/product/category/top").priority(3).build(),
//                CategoryMenuDto.builder().categoryName("KNIT").categoryLink("/product/category/knit").priority(4).build(),
//                CategoryMenuDto.builder().categoryName("SHIRTS").categoryLink("/product/category/shirts").priority(5).build(),
//                CategoryMenuDto.builder().categoryName("PANTS").categoryLink("/product/category/pants").priority(6).build(),
//                CategoryMenuDto.builder().categoryName("SHOES").categoryLink("/product/category/shoes").priority(7).build(),
//                CategoryMenuDto.builder().categoryName("BAG").categoryLink("/product/category/bag").priority(8).build(),
//                CategoryMenuDto.builder().categoryName("ACC").categoryLink("/product/category/acc").priority(9).build(),
//                CategoryMenuDto.builder().categoryName("SALE").categoryLink("/product/category/sale").priority(10).build()
//        );
//    }

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

package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.domain.product.dto.FlatSubCategoryDto;
import com.fast_campus_12.not_found.shop.domain.product.model.Lv1Category;
import com.fast_campus_12.not_found.shop.domain.product.model.Lv2Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // Lv1 조회
    @Select("""
        SELECT * 
        FROM CATEGORY_LV1 
        WHERE IS_ACTIVE = 'Y' 
        ORDER BY SORT_ORDER
    """)
    List<Lv1Category> getLv1Categories();

    // Lv2 기준으로 Lv3를 묶어 조회
    @Select("""
        SELECT 
            l2.NAME AS lv2Name,
            l3.NAME AS categoryName,
            CONCAT('/product/category/', l2.NAME, '/', l3.NAME) AS link
        FROM CATEGORY_LV3 l3
        JOIN CATEGORY_LV2 l2 ON l3.CATEGORY_LV2_ID = l2.CATEGORY_LV2_ID
        WHERE l2.IS_ACTIVE = 'Y'
          AND l3.IS_ACTIVE = 'Y'
        ORDER BY l2.SORT_ORDER, l3.SORT_ORDER
    """)
    List<FlatSubCategoryDto> getAllSubCategoriesGroupedByLv2();

    // Lv1 + Lv2 트리 구조 조회
    @Select("""
        SELECT * 
        FROM CATEGORY_LV1 
        WHERE IS_ACTIVE = 'Y' 
        ORDER BY SORT_ORDER
    """)
    @Results(id = "lv1WithLv2Map", value = {
            @Result(column = "CATEGORY_LV1_ID", property = "lv1Id"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "SORT_ORDER", property = "sortOrder"),
            @Result(column = "IS_ACTIVE", property = "isActive"),
            @Result(property = "children", column = "CATEGORY_LV1_ID",
                    many = @Many(select = "getLv2ByLv1"))
    })
    List<Lv1Category> getLv1CategoriesWithLv2();

    @Select("""
        SELECT * 
        FROM CATEGORY_LV2 
        WHERE CATEGORY_LV1_ID = #{lv1Id} 
          AND IS_ACTIVE = 'Y' 
        ORDER BY SORT_ORDER
    """)
    @Results(id = "lv2Map", value = {
            @Result(column = "CATEGORY_LV2_ID", property = "lv2Id"),
            @Result(column = "CATEGORY_LV1_ID", property = "lv1Id"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "SORT_ORDER", property = "sortOrder"),
            @Result(column = "IS_ACTIVE", property = "isActive")
    })
    List<Lv2Category> getLv2ByLv1(String lv1Id);
}
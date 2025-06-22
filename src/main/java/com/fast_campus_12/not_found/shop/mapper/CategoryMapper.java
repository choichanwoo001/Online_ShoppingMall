package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.dto.CategoryMenuDto;
import com.fast_campus_12.not_found.shop.product.model.Lv1Category;
import com.fast_campus_12.not_found.shop.product.model.Lv2Category;
import com.fast_campus_12.not_found.shop.product.model.Lv3Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // Lv1 조회
    @Select("SELECT * FROM lv1 WHERE is_active = 'Y' ORDER BY sort_order")
    List<Lv1Category> getLv1Categories();

    // Lv3 조회 by Lv2
    @Select("SELECT * FROM lv3 WHERE lv2_id = #{lv2Id} AND is_active = 'Y' ORDER BY sort_order")
    List<Lv3Category> getLv3ByLv2(String lv2Id);

    @Select("SELECT * FROM lv1 WHERE is_active = 'Y' ORDER BY sort_order")
    @Results(id = "lv1WithLv2Map", value = {
            @Result(column = "lv1_id", property = "lv1Id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sort_order", property = "sortOrder"),
            @Result(column = "is_active", property = "isActive"),
            @Result(property = "children", column = "lv1_id",
                    many = @Many(select = "getLv2ByLv1"))
    })
    List<Lv1Category> getLv1CategoriesWithLv2();


    @Select("SELECT * FROM lv2 WHERE lv1_id = #{lv1Id} AND is_active = 'Y' ORDER BY sort_order")
    @Results(id = "lv2Map", value = {
            @Result(column = "lv2_id", property = "lv2Id"),
            @Result(column = "lv1_id", property = "lv1Id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sort_order", property = "sortOrder"),
            @Result(column = "is_active", property = "isActive")
    })
    List<Lv2Category> getLv2ByLv1(String lv1Id);
}
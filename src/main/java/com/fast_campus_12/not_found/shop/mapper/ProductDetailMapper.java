package com.fast_campus_12.not_found.shop.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public class ProductDetailMapper {
    @Select("""
        select PRODUCT_ID,
               PRODUCT_SUMMARY,
               PRODUCT_THUMBNAIL,
               
        FROM PRODUCT
        WHERE id = #{id}
""")

}

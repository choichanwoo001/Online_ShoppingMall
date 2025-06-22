package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.Lv3Category;
import com.fast_campus_12.not_found.shop.product.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("""
        SELECT
            p.product_id AS product_id,
            p.title,
            p.price,
            p.thumbnail,
            p.description AS comment,
            p.enabled,
            
            lv3.lv3_id AS lv3_id,
            lv3.name AS lv3_name,
    
            lv2.lv2_id AS lv2_id,
            lv2.name AS lv2_name,
    
            lv1.lv1_id AS lv1_id,
            lv1.name AS lv1_name,
    
            (
                SELECT COUNT(*)
                FROM review r
                WHERE r.product_id = p.product_id
            ) AS reviewCount
    
        FROM product p
        JOIN lv3 ON p.lv3_id = lv3.lv3_id
        JOIN lv2 ON lv3.lv2_id = lv2.lv2_id
        JOIN lv1 ON lv2.lv1_id = lv1.lv1_id
        WHERE p.enabled = 1
        ORDER BY p.created_at DESC
        LIMIT 10
    """)
    @Results(id = "ProductSummary", value = {
            @Result(property = "id", column = "product_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price"),
            @Result(property = "thumbnail", column = "thumbnail"),
            @Result(property = "comment", column = "comment"),
            @Result(property = "lv3Category.lv3Id", column = "lv3_id"),
            @Result(property = "lv3Category.lv2Id", column = "lv2_id"),
            @Result(property = "lv3Category.name", column = "name"),
            @Result(property = "lv3Category.sortOrder", column = "sort_order"),
            @Result(property = "lv3Category.isActive", column = "is_active")
//            @Result(property = "reviewCount", column = "reviewCount")
    })
    List<Product> findProductSummaryList();
}

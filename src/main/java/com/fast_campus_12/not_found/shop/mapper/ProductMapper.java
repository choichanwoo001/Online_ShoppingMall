package com.fast_campus_12.not_found.shop.mapper;

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
            p.product_id AS id,
            p.title,
            p.price,
            p.thumbnail,
            p.description AS comment,
            (
                SELECT COUNT(*)
                FROM review r
                WHERE r.product_id = p.product_id
            ) AS reviewCount
        FROM product p
        WHERE p.enabled = 1
        ORDER BY p.created_at DESC
        LIMIT 10
    """)
    @Results(id = "ProductSummary", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price"),
            @Result(property = "thumbnail", column = "thumbnail"),
            @Result(property = "comment", column = "comment"),
            @Result(property = "reviewCount", column = "reviewCount")
    })
    List<Product> findProductSummaryList();
}
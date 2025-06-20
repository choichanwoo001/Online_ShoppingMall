package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    // 상품 ID로 상품 하나 조회
    Product findById(Long productId);

    // 모든 상품 목록 조회 (예시)
    // List<Product> findAll();
}
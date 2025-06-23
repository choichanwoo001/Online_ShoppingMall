package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.ProductVariantDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

@Mapper
public interface ProductVariantDetailMapper {
    ProductVariantDetail findByVariantId(@Param("productVariantId") BigInteger productVariantId);
    void insert(ProductVariantDetail detail);
    void update(ProductVariantDetail detail);
    void delete(@Param("productDetailId") BigInteger productDetailId);
}


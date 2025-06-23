package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.ProductVariant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ProductVariantMapper {
    List<ProductVariant> findByProductId(@Param("productId") BigInteger productId);
    ProductVariant findById(@Param("productVariantId") BigInteger productVariantId);
    void insert(ProductVariant variant);
    void update(ProductVariant variant);
    void delete(@Param("productVariantId") BigInteger productVariantId);
}


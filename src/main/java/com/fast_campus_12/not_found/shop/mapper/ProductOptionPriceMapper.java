package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.ProductOptionPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

@Mapper
public interface ProductOptionPriceMapper {
    void insert(ProductOptionPrice price);
    void update(ProductOptionPrice price);
    void delete(@Param("productVariantId") BigInteger productVariantId);
}

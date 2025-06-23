package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.ProductVariantImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ProductVariantImageMapper {
    List<ProductVariantImage> findByVariantId(@Param("variantId") BigInteger variantId);
    void insert(ProductVariantImage image);
    void update(ProductVariantImage image);
    void delete(@Param("imageId") BigInteger imageId);
}

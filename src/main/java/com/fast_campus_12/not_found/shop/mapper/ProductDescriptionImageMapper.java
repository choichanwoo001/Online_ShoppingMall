package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.ProductDescriptionImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ProductDescriptionImageMapper {
    List<ProductDescriptionImage> findByProductId(@Param("productId") BigInteger productId);
    void insert(ProductDescriptionImage image);
    void update(ProductDescriptionImage image);
    void delete(@Param("imageId") BigInteger imageId);
}

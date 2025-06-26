package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.model.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDynamicQueryMapper {
    List<Product> findProductListByCategory(@Param("request") ProductSummaryRequestDto request);
    int countProductListByCategory(@Param("request") ProductSummaryRequestDto request);

    List<Product> findProductListBySpecialTag(@Param("request") ProductSpecialSummaryRequestDto request);
    int countProductListBySpecialTag(@Param("request") ProductSpecialSummaryRequestDto request);
}

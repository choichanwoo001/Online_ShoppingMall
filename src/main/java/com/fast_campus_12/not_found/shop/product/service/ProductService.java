package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;

import java.math.BigInteger;
import java.util.List;

public interface ProductService {
    List<ProductSummaryDto> getSummaryByCategory(String category);
    List<ProductSummaryDto> getSummaryByCategory(String category, String subCategory);
    ProductDetailDto getProductDetailDto(BigInteger productId);
}
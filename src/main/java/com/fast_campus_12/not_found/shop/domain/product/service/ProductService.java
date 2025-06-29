package com.fast_campus_12.not_found.shop.domain.product.service;


import com.fast_campus_12.not_found.shop.domain.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.domain.product.dto.ProductPageDto;

import com.fast_campus_12.not_found.shop.domain.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.domain.product.dto.ProductSummaryRequestDto;

import java.math.BigInteger;

public interface ProductService {
    ProductPageDto getSummaryByCategory(ProductSummaryRequestDto productSummaryRequestDto);
    ProductPageDto getSummaryBySpecialCategory(ProductSpecialSummaryRequestDto productSpecialSummaryRequestDto);
    ProductDetailDto getProductDetailDto(BigInteger productId);
}
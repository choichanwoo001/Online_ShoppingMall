package com.fast_campus_12.not_found.shop.product.service;


import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;

import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;

import java.math.BigInteger;

public interface ProductService {
    ProductPageDto getSummaryByCategory(ProductSummaryRequestDto productSummaryRequestDto);
    ProductPageDto getSummaryBySpecialCategory(ProductSpecialSummaryRequestDto productSpecialSummaryRequestDto);
    ProductDetailDto getProductDetailDto(BigInteger productId);
}
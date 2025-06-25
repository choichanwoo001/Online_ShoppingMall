package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;

public interface ProductService {
    ProductPageDto getSummaryByCategory(ProductSummaryRequestDto productSummaryRequestDto);
}
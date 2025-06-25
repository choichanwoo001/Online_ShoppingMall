package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;

public interface ProductService {
    ProductPageDto getSummaryByCategory(String category, String subCategory, int offset, int limit);
}
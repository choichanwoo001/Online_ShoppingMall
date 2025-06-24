package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.model.Product;

import java.util.List;

public interface ProductService {
    List<ProductSummaryDto> getSummaryByCategory(String category, String subCategory, int offset, int limit);
}
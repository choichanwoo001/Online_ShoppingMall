package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("prod")
public class ProductServiceImpl implements ProductService {
    @Override
    public List<ProductSummaryDto> getSummaryByCategory(String category) {
        return List.of();
    }
}

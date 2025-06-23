package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {
    public ProductDetailDto getProductDetail(Long id) {
        return ProductDetailDto.builder()
                .title("Mock Product")
                .price(BigDecimal.valueOf(12000))
                .thumbnail("/mock-thumbnail.jpg")
                .summary("Mock summary")
                .build();
    }
}

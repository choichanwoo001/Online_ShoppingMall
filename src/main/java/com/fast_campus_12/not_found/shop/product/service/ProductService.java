package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ProductService {
    public default ProductDetailDto getProductDetail(Long id) {
        return ProductDetailDto.builder()
                .title("Mock Product")
                .price(BigDecimal.valueOf(12000))
                .thumbnail("/mock-thumbnail.jpg")
                .summary("Mock summary")
                .build();
    }

    List<ProductSummaryDto> getSummaryByCategory(String category);
}

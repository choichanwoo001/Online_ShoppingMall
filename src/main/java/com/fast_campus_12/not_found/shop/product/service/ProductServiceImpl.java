package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductSummaryDto> getSummaryByCategory(String category) {
        // 실제 로직이 생기기 전까지는 임시 더미 데이터라도 반환
        return List.of(
                ProductSummaryDto.builder()
                        .id(1L)
                        .title("샘플 상품")
                        .price(10000)
                        .thumbnail("/images/sample.jpg")
                        .build()
        );
    }
}

package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.product.model.Product;
import com.fast_campus_12.not_found.shop.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductDetailService {

    private final ProductMapper productMapper;

    @Autowired
    public ProductDetailService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductDetailDto getProductDetail(Long productId) {
        Product product = productMapper.findById(productId);

        if (product == null) {
            return null; // 또는 예외 처리: throw new NotFoundException("상품 없음");
        }

        return ProductDetailDto.builder()
                .title(product.getTitle())
                .price(BigDecimal.valueOf(product.getPrice()))
                .thumbnail(product.getThumbnail())
                .summary(product.getSummary())
                .build();
    }
}
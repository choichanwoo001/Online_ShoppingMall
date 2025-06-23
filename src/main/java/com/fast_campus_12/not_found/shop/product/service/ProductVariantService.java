package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductVariantDto;
import com.fast_campus_12.not_found.shop.product.model.StockStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ProductVariantService {
    public List<ProductVariantDto> getAll() {
        return List.of(
                ProductVariantDto.builder()
                        .productVariantId(BigInteger.ONE)
                        .productId(BigInteger.ONE)
                        .colorId(1)
                        .sku("SKU123")
                        .size("M")
                        .remainingStock(10)
                        .stockStatus(StockStatus.IN_STOCK)
                        .priceModifier(0.0)
                        .imageUrl("/variant.jpg")
                        .build()
        );
    }
}

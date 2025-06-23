package com.fast_campus_12.not_found.shop.product.dto;

import com.fast_campus_12.not_found.shop.product.model.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDto {

    private BigInteger productVariantId;
    private BigInteger productId;
    private Integer colorId;
    private String sku;
    private String size;
    private Integer remainingStock;
    private StockStatus stockStatus;
    private Double priceModifier;
    private String imageUrl;
}


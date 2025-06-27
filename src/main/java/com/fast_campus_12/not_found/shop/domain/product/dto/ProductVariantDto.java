package com.fast_campus_12.not_found.shop.domain.product.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigInteger;

@Data
@Builder
public class ProductVariantDto {
    private int colorId;
    private String colorName;
    private BigInteger sizeId;
    private String sizeName;
    private double price;
}


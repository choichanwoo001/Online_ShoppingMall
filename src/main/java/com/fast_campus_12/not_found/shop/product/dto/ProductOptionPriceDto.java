package com.fast_campus_12.not_found.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionPriceDto {

    private BigInteger productVariantId;

    private Double originalPrice;

    private Double salePrice;

    private Double discountRate;

    private Double extraCharge;
}

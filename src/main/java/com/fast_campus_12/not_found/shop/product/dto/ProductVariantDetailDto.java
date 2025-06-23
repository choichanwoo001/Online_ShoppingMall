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
public class ProductVariantDetailDto {

    private BigInteger productDetailId;
    private BigInteger productVariantId;
    private BigInteger modelId;
    private String deliveryOption;
    private String modelSpecific;
    private String fabricInfo;
    private String fabricManagementInfo;
}

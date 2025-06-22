package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class ProductVariantDetail {

    private BigInteger productDetailId;

    private BigInteger productVariantId;

    private BigInteger modelId;

    private String deliveryOption;

    private String modelSpecific;

    private String fabricInfo;

    private String fabricManagementInfo;
}

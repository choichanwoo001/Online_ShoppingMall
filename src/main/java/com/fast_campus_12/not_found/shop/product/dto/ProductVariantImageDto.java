package com.fast_campus_12.not_found.shop.product.dto;

import com.fast_campus_12.not_found.shop.product.model.ImageFormat;
import com.fast_campus_12.not_found.shop.product.model.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantImageDto {

    private BigInteger imageId;
    private BigInteger variantId;
    private String url;
    private ImageFormat format;
    private ImageType imageType;
    private Integer minWidth;
    private Integer maxWidth;
    private Integer renderPriority;
}

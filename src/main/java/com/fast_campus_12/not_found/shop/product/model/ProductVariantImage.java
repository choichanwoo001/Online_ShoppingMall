package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class ProductVariantImage {

    private BigInteger imageId;

    private BigInteger variantId;

    private String url;

    private ImageFormat format;

    private ImageType imageType;

    private Integer minWidth;

    private Integer maxWidth;

    private Integer renderPriority;

    private LocalDateTime createdAt;
}

enum ImageFormat {
    JPG, PNG, WEBP
}

enum ImageType {
    MAIN, FRONT, BACK, SIDE, ZOOM
}
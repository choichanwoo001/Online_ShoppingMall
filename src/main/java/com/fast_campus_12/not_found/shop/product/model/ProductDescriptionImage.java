package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class ProductDescriptionImage {

    private BigInteger imageId;

    private String showDevice;

    private String url;

    private ImageFormat format;

    private Integer renderPriority;

    private LocalDateTime createdAt;

    private BigInteger productId;
}

enum ImageFormat {
    JPG, PNG, WEBP
}
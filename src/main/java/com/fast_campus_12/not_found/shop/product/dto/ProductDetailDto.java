package com.fast_campus_12.not_found.shop.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class ProductDetailDto {
    private BigInteger productId;
    private String title;
    private String productDescription;
    private String thumbnail;
    private String summary;
    private String code;
    private String comment;
    private String model;
    private String size;
    private ProductVariantDto productVariant;
    private int savePoint;
    private int quantity;
    private List<String> images;
    private List<ColorDto> colors;
    private List<SizeDto> sizes;
    private List<String> detailImages;
    private String detailDescription;
    private List<ReviewDto> reviews;
}


package com.fast_campus_12.not_found.shop.product.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class OrderOptionDto {
    private Long productVariantId;
    private int quantity;
}

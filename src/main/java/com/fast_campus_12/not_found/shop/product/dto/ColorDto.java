package com.fast_campus_12.not_found.shop.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColorDto {
    private int colorId;    // 색상 식별자
    private String name;    // 색상 이름 (예: "빨강")
}

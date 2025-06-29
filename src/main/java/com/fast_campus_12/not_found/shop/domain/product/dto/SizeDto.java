package com.fast_campus_12.not_found.shop.domain.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class SizeDto {
    private BigInteger sizeId;  // 사이즈 식별자
    private String name;        // 사이즈 이름 (예: "S", "M", "L")
}

package com.fast_campus_12.not_found.shop.product.dto;

import com.fast_campus_12.not_found.shop.product.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelDto {

    private BigInteger modelId;
    private String name;
    private Integer height;
    private Integer weight;
    private Gender gender;
    private Integer age;
    private Boolean isActive;
}

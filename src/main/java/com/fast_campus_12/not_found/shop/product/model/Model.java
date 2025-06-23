package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;
import java.math.BigInteger;

@Data
public class Model {

    private BigInteger modelId;

    private String name;

    private Integer height;

    private Integer weight;

    private Gender gender;

    private Integer age;

    private Boolean isActive;
}


package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;
import java.math.BigInteger;

@Data
public class Colors {

    private BigInteger colorId;

    private String name;

    private String hexCode;

    private Boolean isActive;
}
package com.fast_campus_12.not_found.shop.domain.product.model;

import lombok.Data;

@Data
public class Lv3Category {
    private String lv3Id;
    private String lv2Id;
    private String name;
    private Integer sortOrder;
    private String isActive;
}

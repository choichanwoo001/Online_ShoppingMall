package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;

import java.util.List;

@Data
public class Lv2Category {
    private String lv2Id;
    private String lv1Id;
    private String name;
    private Integer sortOrder;
    private String isActive;
    private List<Lv3Category> children;  // 계층 관계 포함
}
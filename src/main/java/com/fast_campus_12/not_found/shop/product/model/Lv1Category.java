package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;

import java.util.List;

@Data
public class Lv1Category {
    private String lv1Id;
    private String name;
    private Integer sortOrder;
    private String isActive;
    private List<Lv2Category> children;  // 계층 관계 포함
}
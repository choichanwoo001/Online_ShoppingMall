package com.fast_campus_12.not_found.shop.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public  class OrderItem {
    private String productName;
    private int quantity;
    private int totalPrice;

}
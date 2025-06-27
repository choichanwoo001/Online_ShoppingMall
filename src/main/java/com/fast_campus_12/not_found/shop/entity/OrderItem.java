package com.fast_campus_12.not_found.shop.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productName;
    private Integer quantity;
    private Integer price;
    private Integer totalPrice;

    // totalPrice 계산 편의 메서드
    public Integer getTotalPrice() {
        if (totalPrice != null) {
            return totalPrice;
        }
        if (quantity != null && price != null) {
            return quantity * price;
        }
        return 0;
    }
}
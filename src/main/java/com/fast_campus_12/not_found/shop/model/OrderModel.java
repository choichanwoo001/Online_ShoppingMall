package com.fast_campus_12.not_found.shop.model;

import lombok.Data;

import java.util.Date;

@Data
public class OrderModel {
        private Long orderId;
        private String loginId;
        private Long userCouponId;
        private String orderNum;
        private Integer totalCount;
        private double totalPrice;
        private String orderStatus;    // pending, confirmed, processing, shipped, delivered, cancelled, refunded
        private double useMile;
        private double orderAmount;
        private double shippingFee;
        private double discountAmount;
        private double finalAmount;
        private Date createdAt;
        private Date updatedAt;
        private Long id2;              // 추가 컬럼
}

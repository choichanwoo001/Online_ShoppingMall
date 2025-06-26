package com.fast_campus_12.not_found.shop.order.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class OrderDto {
        private String orderId;
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

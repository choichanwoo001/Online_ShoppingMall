package com.fast_campus_12.not_found.shop.domain.order.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CouponDto {
    private Long couponId;              // 쿠폰ID(PK)
    private String couponName;          // 쿠폰명
    private String couponType;          // 할인유형(fixed/percentage)
    private BigDecimal discountValue;   // 할인값(금액 또는 비율)
    private BigDecimal minOrderAmount;  // 최소주문금액
    private BigDecimal maxDiscountAmount; // 최대할인금액
    private Integer availablePeriod;    // 발급후사용가능기간(일)
    private Integer totalCnt;           // 총발급수량
    private String duplicateUse;        // 중복사용여부(Y/N)
    private String couponStatus;        // 쿠폰상태(active/inactive/expired)
    private String description;         // 쿠폰설명
    private LocalDateTime startDate;    // 시작일시
    private LocalDateTime endDate;      // 종료일시
    private LocalDateTime createdAt;    // 생성일시
    private String createdBy;           // 생성자
}

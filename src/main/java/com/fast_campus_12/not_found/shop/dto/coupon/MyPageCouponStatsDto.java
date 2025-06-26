package com.fast_campus_12.not_found.shop.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 통계 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageCouponStatsDto {
    private int totalCoupons;        // 전체 쿠폰 수
    private int availableCoupons;    // 사용 가능한 쿠폰 수
    private int usedCoupons;         // 사용된 쿠폰 수
    private int expiredCoupons;      // 만료된 쿠폰 수
}
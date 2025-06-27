package com.fast_campus_12.not_found.shop.domain.cupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 등록 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageCouponRegisterRequest {
    private String couponCode;
}

package com.fast_campus_12.not_found.shop.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 쿠폰 등록 결과 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageCouponRegisterDto {
    private String couponName;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime expireAt;

    /**
     * 할인 표시 텍스트 반환
     */
    public String getDiscountText() {
        if ("PERCENTAGE".equals(discountType)) {
            return discountValue.intValue() + "%";
        } else {
            return "₩" + discountValue.intValue();
        }
    }
}
package com.fast_campus_12.not_found.shop.domain.cupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 사용자 쿠폰 조회 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUserCouponDto {
    private Long userCouponId;
    private Long userId;
    private Long couponId;
    private String couponCode;
    private String couponName;
    private String description;
    private String couponType; // FIXED, PERCENTAGE
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDateTime expireAt;
    private boolean isUsed;
    private LocalDateTime usedAt;
    private LocalDateTime issuedAt;
    private String status; // AVAILABLE, USED, EXPIRED
    private LocalDateTime createdAt;

    /**
     * 할인 표시 텍스트 반환
     */
    public String getDiscountText() {
        if ("PERCENTAGE".equals(couponType)) {
            return discountValue.intValue() + "%";
        } else {
            return "₩" + discountValue.intValue();
        }
    }

    /**
     * 상태 한글 텍스트 반환
     */
    public String getStatusText() {
        switch (status) {
            case "AVAILABLE":
                return "사용가능";
            case "USED":
                return "사용완료";
            case "EXPIRED":
                return "만료";
            default:
                return "알수없음";
        }
    }
}


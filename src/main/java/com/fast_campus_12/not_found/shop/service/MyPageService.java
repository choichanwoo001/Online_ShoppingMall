package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.dto.coupon.*;
import com.fast_campus_12.not_found.shop.mapper.CouponMapper;
import com.fast_campus_12.not_found.shop.mapper.UserCouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MyPageService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    /**
     * 사용자 쿠폰 목록 조회
     */
    public List<MyPageUserCouponDto> getUserCoupons(String loginId) {
        return userCouponMapper.findByLoginIdOrderByCreatedAtDesc(loginId);
    }

    /**
     * 쿠폰 통계 조회
     */
    public MyPageCouponStatsDto getCouponStats(String loginId) {
        LocalDateTime now = LocalDateTime.now();

        int totalCoupons     = userCouponMapper.countByLoginId(loginId);
        int availableCoupons = userCouponMapper.countAvailableByLoginId(loginId, now);
        int usedCoupons      = userCouponMapper.countUsedByLoginId(loginId);
        int expiredCoupons   = userCouponMapper.countExpiredByLoginId(loginId, now);

        return MyPageCouponStatsDto.builder()
                .totalCoupons(totalCoupons)
                .availableCoupons(availableCoupons)
                .usedCoupons(usedCoupons)
                .expiredCoupons(expiredCoupons)
                .build();
    }

    /**
     * 쿠폰 등록
     */
    @Transactional
    public MyPageCouponRegisterDto registerCoupon(String loginId, String couponCode) {
        Map<String, Object> coupon = couponMapper.findByCouponName(couponCode);
        if (coupon == null) {
            throw new IllegalArgumentException("유효하지 않은 쿠폰 코드입니다.");
        }

        Long couponId        = (Long) coupon.get("COUPON_ID");
        String couponStatus  = (String) coupon.get("COUPON_STATUS");
        LocalDateTime start  = (LocalDateTime) coupon.get("START_DATE");
        LocalDateTime end    = (LocalDateTime) coupon.get("END_DATE");
        Integer totalCnt     = (Integer) coupon.get("TOTAL_CNT");
        String duplicateUse  = (String)  coupon.get("DUPLICATE_USE");

        LocalDateTime now = LocalDateTime.now();
        if (!"ACTIVE".equals(couponStatus)) {
            throw new IllegalArgumentException("비활성화된 쿠폰입니다.");
        }
        if (start   != null && now.isBefore(start)) throw new IllegalArgumentException("아직 발급 가능한 기간이 아닙니다.");
        if (end     != null && now.isAfter(end))   throw new IllegalArgumentException("발급 기간이 만료되었습니다.");
        if ("N".equals(duplicateUse)
                && userCouponMapper.existsByLoginIdAndCouponId(loginId, couponId)) {
            throw new IllegalArgumentException("이미 등록된 쿠폰입니다.");
        }
        if (totalCnt != null && totalCnt > 0) {
            int issued = userCouponMapper.countByCouponId(couponId);
            if (issued >= totalCnt) {
                throw new IllegalArgumentException("쿠폰 발급 수량이 모두 소진되었습니다.");
            }
        }

        LocalDateTime expireAt = calculateExpireAt(coupon);
        userCouponMapper.insertByLoginId(loginId, couponId, couponCode, expireAt, now);
        log.info("쿠폰 등록 완료 - 사용자: {}, 쿠폰: {}", loginId, couponCode);

        return MyPageCouponRegisterDto.builder()
                .couponName((String) coupon.get("COUPON_NAME"))
                .discountType((String) coupon.get("COUPON_TYPE"))
                .discountValue((java.math.BigDecimal) coupon.get("DISCOUNT_VALUE"))
                .expireAt(expireAt)
                .build();
    }

    /**
     * 쿠폰 만료일 계산
     */
    private LocalDateTime calculateExpireAt(Map<String, Object> coupon) {
        LocalDateTime now         = LocalDateTime.now();
        Integer availablePeriod   = (Integer) coupon.get("AVAILABLE_PERIOD");
        LocalDateTime endDate     = (LocalDateTime) coupon.get("END_DATE");

        if (availablePeriod != null && availablePeriod > 0) {
            return now.plusDays(availablePeriod);
        } else if (endDate != null) {
            return endDate;
        } else {
            return now.plusDays(30);
        }
    }
}

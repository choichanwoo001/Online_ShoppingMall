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
    public List<MyPageUserCouponDto> getUserCoupons(Long userId) {
        return userCouponMapper.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 쿠폰 통계 조회
     */
    public MyPageCouponStatsDto getCouponStats(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        // 전체 쿠폰 수
        int totalCoupons = userCouponMapper.countByUserId(userId);

        // 사용 가능한 쿠폰 수
        int availableCoupons = userCouponMapper.countByUserIdAndIsUsedFalseAndExpireAtAfter(userId, now);

        // 사용된 쿠폰 수
        int usedCoupons = userCouponMapper.countByUserIdAndIsUsedTrue(userId);

        // 만료된 쿠폰 수
        int expiredCoupons = userCouponMapper.countByUserIdAndIsUsedFalseAndExpireAtBefore(userId, now);

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
    public MyPageCouponRegisterDto registerCoupon(Long userId, String couponCode) {
        // 쿠폰 코드로 쿠폰 조회 (입력된 쿠폰 코드가 실제로는 쿠폰명일 수 있음)
        Map<String, Object> coupon = couponMapper.findByCouponName(couponCode);
        if (coupon == null) {
            throw new IllegalArgumentException("유효하지 않은 쿠폰 코드입니다.");
        }

        Long couponId = (Long) coupon.get("COUPON_ID");
        String couponStatus = (String) coupon.get("COUPON_STATUS");
        LocalDateTime startDate = (LocalDateTime) coupon.get("START_DATE");
        LocalDateTime endDate = (LocalDateTime) coupon.get("END_DATE");
        Integer totalCnt = (Integer) coupon.get("TOTAL_CNT");
        String duplicateUse = (String) coupon.get("DUPLICATE_USE");

        // 쿠폰 활성화 상태 확인
        if (!"ACTIVE".equals(couponStatus)) {
            throw new IllegalArgumentException("비활성화된 쿠폰입니다.");
        }

        // 쿠폰 발급 기간 확인
        LocalDateTime now = LocalDateTime.now();
        if (startDate != null && now.isBefore(startDate)) {
            throw new IllegalArgumentException("아직 발급 가능한 기간이 아닙니다.");
        }
        if (endDate != null && now.isAfter(endDate)) {
            throw new IllegalArgumentException("발급 기간이 만료되었습니다.");
        }

        // 중복 사용 가능 여부 확인
        if ("N".equals(duplicateUse)) {
            boolean alreadyRegistered = userCouponMapper.existsByUserIdAndCouponId(userId, couponId);
            if (alreadyRegistered) {
                throw new IllegalArgumentException("이미 등록된 쿠폰입니다.");
            }
        }

        // 발급 수량 제한 확인
        if (totalCnt != null && totalCnt > 0) {
            int currentIssueCount = userCouponMapper.countByCouponId(couponId);
            if (currentIssueCount >= totalCnt) {
                throw new IllegalArgumentException("쿠폰 발급 수량이 모두 소진되었습니다.");
            }
        }

        // 만료일 계산
        LocalDateTime expireAt = calculateExpireAt(coupon);

        // UserCoupon 등록
        userCouponMapper.insertUserCoupon(userId, couponId, couponCode, expireAt, now);

        log.info("쿠폰 등록 완료 - 사용자: {}, 쿠폰: {}", userId, couponCode);

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
        LocalDateTime now = LocalDateTime.now();

        Integer availablePeriod = (Integer) coupon.get("AVAILABLE_PERIOD");
        LocalDateTime endDate = (LocalDateTime) coupon.get("END_DATE");

        if (availablePeriod != null && availablePeriod > 0) {
            return now.plusDays(availablePeriod);
        } else if (endDate != null) {
            return endDate;
        } else {
            // 기본값: 30일
            return now.plusDays(30);
        }
    }
}
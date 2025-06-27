package com.fast_campus_12.not_found.shop.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 쿠폰 Mapper
 */
@Mapper
public interface CouponMapper {
    // 쿠폰명으로 쿠폰 조회
    @Select("""
        SELECT 
            COUPON_ID,
            COUPON_NAME,
            COUPON_TYPE,
            DISCOUNT_VALUE,
            MIN_ORDER_AMOUNT,
            MAX_DISCOUNT_AMOUNT,
            AVAILABLE_PERIOD,
            TOTAL_CNT,
            DUPLICATE_USE,
            COUPON_STATUS,
            START_DATE,
            END_DATE,
            DESCRIPTION
        FROM COUPON
        WHERE COUPON_NAME = #{couponName}
          AND COUPON_STATUS = 'ACTIVE'
    """)
    Map<String, Object> findByCouponName(@Param("couponName") String couponName);
}
package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.dto.coupon.MyPageUserCouponDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 사용자 쿠폰 Mapper
 */
@Mapper
public interface UserCouponMapper {

    // 사용자별 쿠폰 목록 조회 (생성일 내림차순)
    @Select("""
        SELECT 
            uc.USER_COUPON_ID as userCouponId,
            uc.USER_ID as userId,
            uc.COUPON_ID as couponId,
            uc.COUPON_CODE as couponCode,
            c.COUPON_NAME as couponName,
            c.DESCRIPTION as description,
            c.COUPON_TYPE as couponType,
            c.DISCOUNT_VALUE as discountValue,
            c.MIN_ORDER_AMOUNT as minOrderAmount,
            c.MAX_DISCOUNT_AMOUNT as maxDiscountAmount,
            uc.EXPIRE_AT as expireAt,
            uc.IS_USED as isUsed,
            uc.USED_AT as usedAt,
            uc.ISSUED_AT as issuedAt,
            uc.CREATED_AT as createdAt,
            CASE 
                WHEN uc.IS_USED = 1 THEN 'USED'
                WHEN uc.EXPIRE_AT IS NOT NULL AND uc.EXPIRE_AT < NOW() THEN 'EXPIRED'
                ELSE 'AVAILABLE'
            END AS status
        FROM USER_COUPON uc
        INNER JOIN COUPON c ON uc.COUPON_ID = c.COUPON_ID
        WHERE uc.USER_ID = #{userId}
          AND uc.DELETED_AT IS NULL
        ORDER BY uc.CREATED_AT DESC
    """)
    List<MyPageUserCouponDto> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    // 사용자 전체 쿠폰 수
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON
        WHERE USER_ID = #{userId}
          AND DELETED_AT IS NULL
    """)
    int countByUserId(@Param("userId") Long userId);

    // 사용자 사용 가능한 쿠폰 수
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON
        WHERE USER_ID = #{userId}
          AND IS_USED = 0
          AND (EXPIRE_AT IS NULL OR EXPIRE_AT > #{currentTime})
          AND DELETED_AT IS NULL
    """)
    int countByUserIdAndIsUsedFalseAndExpireAtAfter(@Param("userId") Long userId,
                                                    @Param("currentTime") LocalDateTime currentTime);

    // 사용자 사용된 쿠폰 수
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON
        WHERE USER_ID = #{userId}
          AND IS_USED = 1
          AND DELETED_AT IS NULL
    """)
    int countByUserIdAndIsUsedTrue(@Param("userId") Long userId);

  // 사용자 만료된 쿠폰 수
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON
        WHERE USER_ID = #{userId}
          AND IS_USED = 0
          AND EXPIRE_AT IS NOT NULL
          AND EXPIRE_AT < #{currentTime}
          AND DELETED_AT IS NULL
    """)
    int countByUserIdAndIsUsedFalseAndExpireAtBefore(@Param("userId") Long userId,
                                                     @Param("currentTime") LocalDateTime currentTime);

    // 사용자가 특정 쿠폰을 이미 등록했는지 확인
    @Select("""
        SELECT COUNT(*) > 0
        FROM USER_COUPON
        WHERE USER_ID = #{userId}
          AND COUPON_ID = #{couponId}
          AND DELETED_AT IS NULL
    """)
    boolean existsByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    // 특정 쿠폰의 총 발급 수량
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON
        WHERE COUPON_ID = #{couponId}
          AND DELETED_AT IS NULL
    """)
    int countByCouponId(@Param("couponId") Long couponId);

    // 사용자 쿠폰 등록
    @Insert("""
        INSERT INTO USER_COUPON (
            USER_ID,
            COUPON_ID,
            COUPON_CODE,
            ISSUED_AT,
            IS_USED,
            EXPIRE_AT,
            CREATED_AT
        ) VALUES (
            #{userId},
            #{couponId},
            #{couponCode},
            #{createdAt},
            0,
            #{expireAt},
            #{createdAt}
        )
    """)
    int insertUserCoupon(@Param("userId") Long userId,
                         @Param("couponId") Long couponId,
                         @Param("couponCode") String couponCode,
                         @Param("expireAt") LocalDateTime expireAt,
                         @Param("createdAt") LocalDateTime createdAt);
}
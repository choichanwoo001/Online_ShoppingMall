package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.dto.coupon.MyPageUserCouponDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 쿠폰 Mapper
 */
@Mapper
public interface UserCouponMapper {

    // 사용자별 쿠폰 목록 조회 (생성일 내림차순) by loginId
    @Select("""
        SELECT 
            uc.USER_COUPON_ID   as userCouponId,
            uc.USER_ID          as userId,
            uc.COUPON_ID        as couponId,
            uc.COUPON_CODE      as couponCode,
            c.COUPON_NAME       as couponName,
            c.DESCRIPTION       as description,
            c.COUPON_TYPE       as couponType,
            c.DISCOUNT_VALUE    as discountValue,
            c.MIN_ORDER_AMOUNT  as minOrderAmount,
            c.MAX_DISCOUNT_AMOUNT as maxDiscountAmount,
            uc.EXPIRE_AT        as expireAt,
            uc.IS_USED          as isUsed,
            uc.USED_AT          as usedAt,
            uc.ISSUED_AT        as issuedAt,
            uc.CREATED_AT       as createdAt,
            CASE 
              WHEN uc.IS_USED = 1 THEN 'USED'
              WHEN uc.EXPIRE_AT IS NOT NULL AND uc.EXPIRE_AT < NOW() THEN 'EXPIRED'
              ELSE 'AVAILABLE'
            END AS status
        FROM USER_COUPON uc
        JOIN USERS u    ON uc.USER_ID = u.USER_ID
        JOIN COUPON c   ON uc.COUPON_ID = c.COUPON_ID
        WHERE u.LOGIN_ID = #{loginId}
          AND uc.DELETED_AT IS NULL
        ORDER BY uc.CREATED_AT DESC
    """)
    List<MyPageUserCouponDto> findByLoginIdOrderByCreatedAtDesc(@Param("loginId") String loginId);

    // 사용자 전체 쿠폰 수 by loginId
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON uc
        JOIN USERS u ON uc.USER_ID = u.USER_ID
        WHERE u.LOGIN_ID = #{loginId}
          AND uc.DELETED_AT IS NULL
    """)
    int countByLoginId(@Param("loginId") String loginId);

    // 사용자 사용 가능한 쿠폰 수 by loginId
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON uc
        JOIN USERS u ON uc.USER_ID = u.USER_ID
        WHERE u.LOGIN_ID = #{loginId}
          AND uc.IS_USED = 0
          AND (uc.EXPIRE_AT IS NULL OR uc.EXPIRE_AT > #{currentTime})
          AND uc.DELETED_AT IS NULL
    """)
    int countAvailableByLoginId(@Param("loginId") String loginId,
                                @Param("currentTime") LocalDateTime currentTime);

    // 사용자 사용된 쿠폰 수 by loginId
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON uc
        JOIN USERS u ON uc.USER_ID = u.USER_ID
        WHERE u.LOGIN_ID = #{loginId}
          AND uc.IS_USED = 1
          AND uc.DELETED_AT IS NULL
    """)
    int countUsedByLoginId(@Param("loginId") String loginId);

    // 사용자 만료된 쿠폰 수 by loginId
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON uc
        JOIN USERS u ON uc.USER_ID = u.USER_ID
        WHERE u.LOGIN_ID = #{loginId}
          AND uc.IS_USED = 0
          AND uc.EXPIRE_AT IS NOT NULL
          AND uc.EXPIRE_AT < #{currentTime}
          AND uc.DELETED_AT IS NULL
    """)
    int countExpiredByLoginId(@Param("loginId") String loginId,
                              @Param("currentTime") LocalDateTime currentTime);

    // 사용자가 특정 쿠폰을 이미 등록했는지 확인 by loginId and couponId
    @Select("""
        SELECT COUNT(*) > 0
        FROM USER_COUPON uc
        JOIN USERS u ON uc.USER_ID = u.USER_ID
        WHERE u.LOGIN_ID = #{loginId}
          AND uc.COUPON_ID = #{couponId}
          AND uc.DELETED_AT IS NULL
    """)
    boolean existsByLoginIdAndCouponId(@Param("loginId") String loginId,
                                       @Param("couponId") Long couponId);

    // 특정 쿠폰의 총 발급 수량 (unchanged)
    @Select("""
        SELECT COUNT(*)
        FROM USER_COUPON
        WHERE COUPON_ID = #{couponId}
          AND DELETED_AT IS NULL
    """)
    int countByCouponId(@Param("couponId") Long couponId);

    // 사용자 쿠폰 등록: 먼저 loginId -> USER_ID 변환 위해 subselect
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
            (SELECT USER_ID FROM USERS WHERE LOGIN_ID = #{loginId}),
            #{couponId},
            #{couponCode},
            #{createdAt},
            0,
            #{expireAt},
            #{createdAt}
        )
    """)
    int insertByLoginId(@Param("loginId") String loginId,
                        @Param("couponId") Long couponId,
                        @Param("couponCode") String couponCode,
                        @Param("expireAt") LocalDateTime expireAt,
                        @Param("createdAt") LocalDateTime createdAt);
}

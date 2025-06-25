package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.order.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("""
    SELECT 
      user_id AS userId,
      email,
      name,
      phone_number AS phoneNumber,
      birth_date AS birthDate,
      gender,
      job_code AS jobCode
    FROM USER_DETAIL 
    WHERE user_id = #{userId}
""")
    UserDetailDto finUserDetailByUserId(Long userId); // 유저 상세정보 조회

    @Select("""
    SELECT
        user_id AS loginId,
        road_address_1 AS roadAddress1,
        road_address_2 AS roadAddress2,
        jibun_address AS jibunAddress,
        detail_address AS detailAddress,
        english_address AS englishAddress,
        zip_code AS zipCode,
        address_name AS addressName
    FROM default_user_address
    WHERE user_id = #{loginId}
""")
    UserAddressDto findUserAddressByUserId(Long userId); // 유저 상세주소 조회

    @Select("""
        SELECT
            p.PRODUCT_TITLE AS productName,
            ci.QUANTITY AS quantity,
            (ci.QUANTITY * p.PRODUCT_PRICE) AS Price
        FROM
            CART_ITEM ci
        JOIN
            CART c ON ci.CART_ID = c.CART_ID
        JOIN
            PRODUCT p ON ci.PRODUCT_ID = p.PRODUCT_ID
        WHERE
            c.USER_ID = #{userId} -- 매개변수 userId를 SQL에 바인딩
    """)
    List<ProductOrderInfoDto> findCartItemsForOrderByUserId(Long userId); // 장바구니에서 주문상품 조회

    @Select("""
        SELECT
            c.COUPON_ID AS couponId,
            c.COUPON_NAME AS couponName,
            c.COUPON_TYPE AS couponType,
            c.DISCOUNT_VALUE AS discountValue,
            c.MIN_ORDER_AMOUNT AS minOrderAmount,
            c.MAX_DISCOUNT_AMOUNT AS maxDiscountAmount,
            c.AVAILABLE_PERIOD AS availablePeriod,
            c.TOTAL_CNT AS totalCnt,
            c.DUPLICATE_USE AS duplicateUse,
            c.COUPON_STATUS AS couponStatus,
            c.DESCRIPTION AS description,
            c.START_DATE AS startDate,
            c.END_DATE AS endDate,
            c.CREATED_AT AS createdAt,
            c.CREATED_BY AS createdBy
        FROM
            USER_COUPON uc
        JOIN
            COUPON c ON uc.COUPON_ID = c.COUPON_ID
        WHERE
            uc.USER_ID = #{userId}
    """)
    List<CouponDto> findUserCouponsByUserId(Long userId); // user_id로 보유 쿠폰 조회

    @Select("""
        SELECT
            m.USER_ID AS userId,
            (m.TOTAL_EARNED - m.TOTAL_USED) AS availableMileage
        FROM
            MILEAGE m
        WHERE
            m.USER_ID = #{userId}
    """)
    MileageDto findAvailableMileageByUserId(@Param("userId") Long userId); // user_id로 마일리지 조회
}



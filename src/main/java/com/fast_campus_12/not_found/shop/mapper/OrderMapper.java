package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.domain.order.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("""
  SELECT
      ud.USER_ID as userId,
      ud.EMAIL as email,
      ud.NAME as name,
      ud.PHONE_NUMBER as phoneNumber,
      ud.BIRTH_DATE as birthDate,
      ud.GENDER as gender,
      ud.JOB_CODE as jobCode
  FROM USERS u
  JOIN USER_DETAIL ud ON u.USER_ID = ud.USER_ID
  WHERE u.LOGIN_ID = #{loginId}
""")
    UserDetailDto finUserDetailByUserId(String loginID); // 유저 상세정보 조회

    @Select("""
    SELECT
        da.USER_ID AS userId,
        da.ROAD_ADDRESS_1 AS roadAddress1,
        da.ROAD_ADDRESS_2 AS roadAddress2,
        da.JIBUN_ADDRESS AS jibunAddress,
        da.DETAIL_ADDRESS AS detailAddress,
        da.ENGLISH_ADDRESS AS englishAddress,
        da.ZIP_CODE AS zipCode,
        da.ADDRESS_NAME AS addressName
    FROM USERS u
    JOIN DEFAULT_USER_ADDRESS da ON u.USER_ID = da.USER_ID
    WHERE u.LOGIN_ID = #{loginId}
""")
    UserAddressDto findUserAddressByUserId(String loginID); // 유저 상세주소 조회

    @Select("""
        SELECT
            p.PRODUCT_TITLE AS productName,
            ci.QUANTITY AS quantity,
            (ci.QUANTITY * p.PRODUCT_PRICE) AS Price
        FROM USERS u
        JOIN CART c ON u.USER_ID = c.USER_ID
        JOIN CART_ITEM ci ON c.CART_ID = ci.CART_ID
        JOIN PRODUCT p ON ci.PRODUCT_ID = p.PRODUCT_ID
        WHERE u.LOGIN_ID = #{loginId} -- 매개변수 userId를 SQL에 바인딩
    """)
    List<ProductOrderInfoDto> findCartItemsForOrderByUserId(String loginID); // 장바구니에서 주문상품 조회

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
       FROM USERS u
       JOIN USER_COUPON uc ON u.USER_ID = uc.USER_ID
       JOIN COUPON c ON uc.COUPON_ID = c.COUPON_ID
       WHERE u.LOGIN_ID = #{loginId}
   """)
    List<CouponDto> findUserCouponsByUserId(String loginID); // user_id로 보유 쿠폰 조회

    @Select("""
        SELECT
            (m.TOTAL_EARNED - m.TOTAL_USED) AS availableMileage
        FROM USERS u
        JOIN MILEAGE m ON u.USER_ID = m.USER_ID
        WHERE u.LOGIN_ID = #{loginId}
    """)
    MileageDto findAvailableMileageByUserId(String loginID); // user_id로 마일리지 조회
}



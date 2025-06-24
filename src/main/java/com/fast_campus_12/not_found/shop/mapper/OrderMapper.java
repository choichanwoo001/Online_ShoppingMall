package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.order.dto.OrderDto;
import com.fast_campus_12.not_found.shop.order.dto.UserAddressDto;
import com.fast_campus_12.not_found.shop.order.dto.UserDetailDto;
import org.apache.ibatis.annotations.*;

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
    FROM user_detail 
    WHERE user_id = #{userId}
""")
    UserDetailDto findByUserId(String userId);

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
    UserAddressDto findAddressByUserId(String userId);
//    @Insert("insert  ")


}

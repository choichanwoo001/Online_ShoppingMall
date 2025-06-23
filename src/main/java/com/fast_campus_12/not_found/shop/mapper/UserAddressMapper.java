package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.UserAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAddressMapper {

    @Insert("INSERT INTO default_user_address (user_id, road_address_1, road_address_2, jibun_address, detail_address) " +
            "VALUES (#{userId}, #{roadAddress1}, #{roadAddress2}, #{jibunAddress}, #{detailAddress})")
    void insertUserAddress(UserAddress userAddress);

    @Select("SELECT * FROM default_user_address WHERE user_id = #{userId}")
    UserAddress findByUserId(@Param("userId") Long userId);
}

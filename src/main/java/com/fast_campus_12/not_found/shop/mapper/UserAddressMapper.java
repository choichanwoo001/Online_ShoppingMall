package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.UserAddress;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserAddressMapper {

    @Insert("INSERT INTO default_user_address (user_id, road_address_1, road_address_2, jibun_address, detail_address) " +
            "VALUES (#{userId}, #{roadAddress1}, #{roadAddress2}, #{jibunAddress}, #{detailAddress})")
    void insertUserAddress(UserAddress userAddress);

    @Select("SELECT * FROM default_user_address WHERE user_id = #{userId}")
    UserAddress findByUserId(@Param("userId") String userId);

    @Update("UPDATE default_user_address SET " +
            "road_address_1 = #{roadAddress1}, road_address_2 = #{roadAddress2}, " +
            "jibun_address = #{jibunAddress}, detail_address = #{detailAddress}, " +
            "english_address = #{englishAddress}, zip_code = #{zipCode}, address_name = #{addressName} " +
            "WHERE user_id = #{userId}")
    void updateUserAddress(UserAddress userAddress);

    // 주소가 없는 경우 새로 삽입하는 메서드 (UPSERT 방식)
    @Insert("INSERT INTO default_user_address " +
            "(user_id, road_address_1, road_address_2, jibun_address, detail_address, english_address, zip_code, address_name) " +
            "VALUES (#{userId}, #{roadAddress1}, #{roadAddress2}, #{jibunAddress}, #{detailAddress}, #{englishAddress}, #{zipCode}, #{addressName}) " +
            "ON DUPLICATE KEY UPDATE " +
            "road_address_1 = VALUES(road_address_1), road_address_2 = VALUES(road_address_2), " +
            "jibun_address = VALUES(jibun_address), detail_address = VALUES(detail_address), " +
            "english_address = VALUES(english_address), zip_code = VALUES(zip_code), address_name = VALUES(address_name)")
    void upsertUserAddress(UserAddress userAddress);


}

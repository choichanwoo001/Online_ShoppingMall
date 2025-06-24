package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.UserAddress;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserAddressMapper {

    /**
     * 사용자 주소 등록 (USER_ID는 FK로 USERS 테이블의 PK 참조)
     */
    @Insert("INSERT INTO DEFAULT_USER_ADDRESS " +
            "(USER_ID, ROAD_ADDRESS_1, ROAD_ADDRESS_2, JIBUN_ADDRESS, DETAIL_ADDRESS, " +
            "ENGLISH_ADDRESS, ZIP_CODE, ADDRESS_NAME) " +
            "VALUES (#{userId}, #{roadAddress1}, #{roadAddress2}, #{jibunAddress}, #{detailAddress}, " +
            "#{englishAddress}, #{zipCode}, #{addressName})")
    void insertUserAddress(UserAddress userAddress);

    /**
     * USER_ID로 사용자 주소 조회
     */
    @Select("SELECT USER_ID as userId, ROAD_ADDRESS_1 as roadAddress1, ROAD_ADDRESS_2 as roadAddress2, " +
            "JIBUN_ADDRESS as jibunAddress, DETAIL_ADDRESS as detailAddress, " +
            "ENGLISH_ADDRESS as englishAddress, ZIP_CODE as zipCode, ADDRESS_NAME as addressName " +
            "FROM DEFAULT_USER_ADDRESS WHERE USER_ID = #{userId}")
    UserAddress findByUserId(@Param("userId") Long userId);

    /**
     * 사용자 주소 수정
     */
    @Update("UPDATE DEFAULT_USER_ADDRESS SET " +
            "ROAD_ADDRESS_1 = #{roadAddress1}, ROAD_ADDRESS_2 = #{roadAddress2}, " +
            "JIBUN_ADDRESS = #{jibunAddress}, DETAIL_ADDRESS = #{detailAddress}, " +
            "ENGLISH_ADDRESS = #{englishAddress}, ZIP_CODE = #{zipCode}, ADDRESS_NAME = #{addressName} " +
            "WHERE USER_ID = #{userId}")
    void updateUserAddress(UserAddress userAddress);

    /**
     * 주소 UPSERT (있으면 수정, 없으면 삽입)
     */
    @Insert("INSERT INTO DEFAULT_USER_ADDRESS " +
            "(USER_ID, ROAD_ADDRESS_1, ROAD_ADDRESS_2, JIBUN_ADDRESS, DETAIL_ADDRESS, " +
            "ENGLISH_ADDRESS, ZIP_CODE, ADDRESS_NAME) " +
            "VALUES (#{userId}, #{roadAddress1}, #{roadAddress2}, #{jibunAddress}, #{detailAddress}, " +
            "#{englishAddress}, #{zipCode}, #{addressName}) " +
            "ON DUPLICATE KEY UPDATE " +
            "ROAD_ADDRESS_1 = VALUES(ROAD_ADDRESS_1), ROAD_ADDRESS_2 = VALUES(ROAD_ADDRESS_2), " +
            "JIBUN_ADDRESS = VALUES(JIBUN_ADDRESS), DETAIL_ADDRESS = VALUES(DETAIL_ADDRESS), " +
            "ENGLISH_ADDRESS = VALUES(ENGLISH_ADDRESS), ZIP_CODE = VALUES(ZIP_CODE), " +
            "ADDRESS_NAME = VALUES(ADDRESS_NAME)")
    void upsertUserAddress(UserAddress userAddress);

}
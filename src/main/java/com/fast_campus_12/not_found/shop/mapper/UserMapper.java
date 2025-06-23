// UserMapper.java (어노테이션 방식 매퍼)
package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.User;
import com.fast_campus_12.not_found.shop.entity.UserAddress;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

//    @Select("SELECT COUNT(*) FROM USERS WHERE user_id = #{userId}")
//    int countByUserId(@Param("userId") String userId);
//
//    @Select("SELECT COUNT(*) FROM USERS WHERE email = #{email}")
//    int countByEmail(@Param("email") String email);

    // EXISTS 쿼리 사용 (더 효율적)
    @Select("SELECT EXISTS(SELECT 1 FROM USERS WHERE user_id = #{userId})")
    boolean existsByUserId(@Param("userId") String userId);

    @Select("SELECT EXISTS(SELECT 1 FROM USERS WHERE email = #{email})")
    boolean existsByEmail(@Param("email") String email);

    @Insert("INSERT INTO USERS (user_id, password, is_activate, created_at, updated_at, role, is_deleted, deleted_at) " +
            "VALUES (#{userId}, #{password}, #{isActivate}, #{createdAt}, #{updatedAt}, #{role}, #{isDeleted}, #{deletedAt})")
    void insertUser(User user);

//    @Insert("INSERT INTO default_user_address " +
//            "(user_id, road_address_1, road_address_2, jibun_address, detail_address, english_address, zip_code, address_name) " +
//            "VALUES (#{userId}, #{roadAddress1}, #{roadAddress2}, #{jibunAddress}, #{detailAddress}, #{englishAddress}, #{zipCode}, #{addressName})")
//    void insertUserAddress(UserAddress userAddress);
//
//    @Select("SELECT user_id, road_address_1 as roadAddress1, road_address_2 as roadAddress2, " +
//            "jibun_address as jibunAddress, detail_address as detailAddress, " +
//            "english_address as englishAddress, zip_code as zipCode, address_name as addressName " +
//            "FROM default_user_address WHERE user_id = #{userId}")
//    UserAddress findByUserId(@Param("userId") String userId);
//
//    @Update("UPDATE default_user_address SET " +
//            "road_address_1 = #{roadAddress1}, road_address_2 = #{roadAddress2}, " +
//            "jibun_address = #{jibunAddress}, detail_address = #{detailAddress}, " +
//            "english_address = #{englishAddress}, zip_code = #{zipCode}, address_name = #{addressName} " +
//            "WHERE user_id = #{userId}")
//    int updateUserAddress(UserAddress userAddress);
//
//    @Delete("DELETE FROM default_user_address WHERE user_id = #{userId}")
//    int deleteByUserId(@Param("userId") String userId);

    @Select("SELECT u.user_id as id, u.user_id, u.password, u.name as userName, " +
            "u.email, u.phone_number as mobilePhone, u.is_activate as isActive, " +
            "u.created_at as createdAt, u.updated_at as updatedAt, u.role, u.is_deleted as isDeleted " +
            "FROM USERS u WHERE u.user_id = #{userId}")
    User selectByUserId(@Param("userId") String userId);

    @Select("SELECT u.user_id as id, u.user_id, u.password, u.name as userName, " +
            "u.email, u.phone_number as mobilePhone, u.is_activate as isActive, " +
            "u.created_at as createdAt, u.updated_at as updatedAt, u.role, u.is_deleted as isDeleted " +
            "FROM USERS u WHERE u.email = #{email}")
    User selectByEmail(@Param("email") String email);

    @Update("UPDATE USERS SET password = #{password}, name = #{userName}, " +
            "email = #{email}, phone_number = #{mobilePhone}, updated_at = #{updatedAt} " +
            "WHERE user_id = #{id}")
    int updateUser(User user);

    @Delete("DELETE FROM USERS WHERE user_id = #{id}")
    int deleteUser(@Param("id") Long id);

    @Update("UPDATE USERS SET is_activate = #{isActive} WHERE user_id = #{id}")
    int updateUserStatus(@Param("id") Long id, @Param("isActive") Boolean isActive);
}
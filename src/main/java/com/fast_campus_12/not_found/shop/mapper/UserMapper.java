// UserMapper.java (어노테이션 방식 매퍼)
package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.User;
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

    @Insert("INSERT INTO USERS (user_id, password, name, email, phone_number, " +
            "is_activate, created_at, updated_at, role, is_deleted) " +
            "VALUES (#{userId}, #{password}, #{userName}, #{email}, #{mobilePhone}, " +
            "#{isActive}, #{createdAt}, #{updatedAt}, 'USER', #{isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "user_id")
    int insertUser(User user);

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
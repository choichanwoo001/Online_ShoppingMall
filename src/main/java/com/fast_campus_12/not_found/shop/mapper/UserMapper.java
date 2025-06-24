package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * LOGIN_ID 중복 확인 (사용자가 입력하는 아이디)
     */
    @Select("SELECT EXISTS(SELECT 1 FROM USERS WHERE LOGIN_ID = #{loginId})")
    boolean existsByLoginId(@Param("loginId") String loginId);

    /**
     * 사용자 등록 (USER_ID는 AUTO_INCREMENT)
     */
    @Insert("INSERT INTO USERS (LOGIN_ID, PASSWORD, IS_ACTIVATE, ROLE, IS_DELETED) " +
            "VALUES (#{loginId}, #{password}, #{isActivate}, #{role}, #{isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(User user);

    /**
     * LOGIN_ID로 사용자 조회
     */
    @Select("SELECT USER_ID as userId, LOGIN_ID as loginId, PASSWORD as password, " +
            "IS_ACTIVATE as isActivate, CREATED_AT as createdAt, UPDATED_AT as updatedAt, " +
            "DELETED_AT as deletedAt, ROLE as role, IS_DELETED as isDeleted " +
            "FROM USERS WHERE LOGIN_ID = #{loginId}")
    User findByLoginId(@Param("loginId") String loginId);

    /**
     * USER_ID(PK)로 사용자 조회
     */
    @Select("SELECT USER_ID as userId, LOGIN_ID as loginId, PASSWORD as password, " +
            "IS_ACTIVATE as isActivate, CREATED_AT as createdAt, UPDATED_AT as updatedAt, " +
            "DELETED_AT as deletedAt, ROLE as role, IS_DELETED as isDeleted " +
            "FROM USERS WHERE USER_ID = #{userId}")
    User findByUserId(@Param("userId") Long userId);

    /**
     * 비밀번호 업데이트
     */
    @Update("UPDATE USERS SET PASSWORD = #{password}, UPDATED_AT = CURRENT_TIMESTAMP WHERE USER_ID = #{userId}")
    void updateUserPassword(User user);

    /**
     * LOGIN_ID로 비밀번호 업데이트
     */
    @Update("UPDATE USERS SET PASSWORD = #{hashedPassword}, UPDATED_AT = CURRENT_TIMESTAMP WHERE LOGIN_ID = #{loginId}")
    int updatePassword(@Param("loginId") String loginId, @Param("hashedPassword") String hashedPassword);

    /**
     * 이메일로 사용자 조회 (JOIN 필요 - UserDetail 테이블과)
     */
    @Select("SELECT u.USER_ID as userId, u.LOGIN_ID as loginId, u.PASSWORD as password, " +
            "u.IS_ACTIVATE as isActivate, u.CREATED_AT as createdAt, u.UPDATED_AT as updatedAt, " +
            "u.DELETED_AT as deletedAt, u.ROLE as role, u.IS_DELETED as isDeleted " +
            "FROM USERS u " +
            "JOIN USER_DETAIL ud ON u.USER_ID = ud.USER_ID " +
            "WHERE ud.EMAIL = #{email}")
    User selectByEmail(@Param("email") String email);

    /**
     * 사용자 정보 수정
     */
    @Update("UPDATE USERS SET IS_ACTIVATE = #{isActivate}, ROLE = #{role}, " +
            "IS_DELETED = #{isDeleted}, UPDATED_AT = CURRENT_TIMESTAMP " +
            "WHERE USER_ID = #{userId}")
    int updateUser(User user);

    /**
     * 사용자 삭제 (물리적 삭제)
     */
    @Delete("DELETE FROM USERS WHERE USER_ID = #{userId}")
    int deleteUser(@Param("userId") Long userId);

    /**
     * 사용자 활성화 상태 변경
     */
    @Update("UPDATE USERS SET IS_ACTIVATE = #{isActive}, UPDATED_AT = CURRENT_TIMESTAMP WHERE USER_ID = #{userId}")
    int updateUserStatus(@Param("userId") Long userId, @Param("isActive") Boolean isActive);
}
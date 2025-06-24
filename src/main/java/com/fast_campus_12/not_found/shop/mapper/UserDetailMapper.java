package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.UserDetail;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDetailMapper {

    /**
     * 사용자 상세정보 등록 (USER_ID는 FK로 USERS 테이블의 PK 참조)
     */
    @Insert("INSERT INTO USER_DETAIL (USER_ID, EMAIL, NAME, PHONE_NUMBER, BIRTH_DATE, GENDER, JOB_CODE) " +
            "VALUES (#{userId}, #{email}, #{name}, #{phoneNumber}, #{birthDate}, #{gender}, #{jobCode})")
    void insertUserDetail(UserDetail userDetail);

    /**
     * USER_ID로 사용자 상세정보 조회
     */
    @Select("SELECT USER_ID as userId, EMAIL as email, NAME as name, PHONE_NUMBER as phoneNumber, " +
            "BIRTH_DATE as birthDate, GENDER as gender, JOB_CODE as jobCode " +
            "FROM USER_DETAIL WHERE USER_ID = #{userId}")
    UserDetail findByUserId(@Param("userId") Long userId);

    /**
     * 이메일 중복 확인
     */
    @Select("SELECT EXISTS(SELECT 1 FROM USER_DETAIL WHERE EMAIL = #{email})")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 사용자 상세정보 수정
     */
    @Update("UPDATE USER_DETAIL SET " +
            "EMAIL = #{email}, NAME = #{name}, PHONE_NUMBER = #{phoneNumber}, " +
            "GENDER = #{gender}, BIRTH_DATE = #{birthDate}, JOB_CODE = #{jobCode} " +
            "WHERE USER_ID = #{userId}")
    void updateUserDetail(UserDetail userDetail);

    /**
     * 특정 사용자를 제외한 이메일 중복 확인 (수정 시 사용)
     */
    @Select("SELECT EXISTS(SELECT 1 FROM USER_DETAIL WHERE EMAIL = #{email} AND USER_ID != #{userId})")
    boolean existsByEmailExcludingUserId(@Param("email") String email, @Param("userId") Long userId);

}
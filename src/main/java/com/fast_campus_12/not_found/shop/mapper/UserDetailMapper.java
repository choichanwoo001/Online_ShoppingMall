package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.UserDetail;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDetailMapper {

    @Insert("INSERT INTO USER_DETAIL (user_id, email, name, phone_number, birth_date, gender) " +
            "VALUES (#{userId}, #{email}, #{name}, #{phoneNumber}, #{birthDate}, #{gender})")
    void insertUserDetail(UserDetail userDetail);

    @Select("SELECT * FROM USER_DETAIL WHERE user_id = #{userId}")
    UserDetail findByUserId(@Param("userId") String userId);

    @Select("SELECT EXISTS(SELECT 1 FROM USER_DETAIL WHERE email = #{email})")
    boolean existsByEmail(@Param("email") String email);

    @Update("UPDATE USER_DETAIL SET " +
            "email = #{email}, name = #{name}, phone_number = #{phoneNumber}, " +
            "gender = #{gender}, birth_date = #{birthDate} " +
            "WHERE user_id = #{userId}")
    void updateUserDetail(UserDetail userDetail);

    @Select("SELECT EXISTS(SELECT 1 FROM USER_DETAIL WHERE email = #{email} AND user_id != #{userId})")
    boolean existsByEmailExcludingUserId(@Param("email") String email, @Param("userId") String userId);

}

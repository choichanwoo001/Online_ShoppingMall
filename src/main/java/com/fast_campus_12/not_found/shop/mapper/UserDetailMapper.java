package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.UserDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDetailMapper {

    @Insert("INSERT INTO USER_DETAIL (user_id, email, name, phone_number, birth_date, gender) " +
            "VALUES (#{userId}, #{email}, #{name}, #{phoneNumber}, #{birthDate}, #{gender})")
    void insertUserDetail(UserDetail userDetail);

    @Select("SELECT * FROM USER_DETAIL WHERE user_id = #{userId}")
    UserDetail findByUserId(@Param("userId") Long userId);

    @Select("SELECT EXISTS(SELECT 1 FROM USER_DETAIL WHERE email = #{email})")
    boolean existsByEmail(@Param("email") String email);
}

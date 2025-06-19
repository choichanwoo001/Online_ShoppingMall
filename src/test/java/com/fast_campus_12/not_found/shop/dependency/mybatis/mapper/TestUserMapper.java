package com.fast_campus_12.not_found.shop.dependency.mybatis.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TestUserMapper {

    @Select("SELECT id, pw FROM test_user WHERE id = #{id}")
    TestUser findById(String id);

    @Select("SELECT * FROM test_user")
    List<TestUser> selectAll();

    @Insert("INSERT INTO test_user(id, pw) VALUES(#{id}, #{pw})")
    void insert(TestUser testUser);

    @Update("UPDATE test_user SET pw = #{pw} WHERE id = #{id}")
    void update(TestUser testUser);

    @Delete("DELETE FROM test_user WHERE id = #{id}")
    void delete(String id);
}

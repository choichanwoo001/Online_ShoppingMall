package com.fast_campus_12.not_found.shop.dependency.mybatis.mapper;

import com.fast_campus_12.not_found.shop.config.MyBatisConfig;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MyBatisConfig.class)
public class SimpleMapperTest {

    @Autowired
    private TestUserMapper testUserMapper;

    @Test
    void testInsertAndSelect() {
        TestUser testUser = new TestUser("testUser1", "testPw1"); // id, pw입력
        testUserMapper.insert(testUser); // insert메서드

        TestUser found = testUserMapper.findById("testUser1"); // id로 셀렉
        assertNotNull(found);
        assertEquals("testPw1", found.getPw());
        System.out.println(testUser.getIdAndPw());
    }

    @Test
    public void testSelectById() {
        TestUser testUser = testUserMapper.findById("admin");

        assertNotNull(testUser, "해당 ID의 유저가 존재해야 한다");
        System.out.println(testUser.getIdAndPw());
    }

    @Test
    void testSelectAll() {
        List<TestUser> testUsers = testUserMapper.selectAll();
        assertNotNull(testUsers);
        assertFalse(testUsers.isEmpty());
        testUsers.forEach(System.out::println);
    }

    @Test
    void testUpdate() {
        TestUser testUser = testUserMapper.findById("updateUser");

        testUser.setPw("testpw2");
        testUserMapper.update(testUser);

        TestUser updated = testUserMapper.findById("updateUser");
        assertEquals("testpw2", updated.getPw());
        System.out.println(testUser.getIdAndPw());
    }

    @Test
    void testDelete() {
//        TestUser testUser = new TestUser("deleteUser", "pw1");
//        testUserMapper.insert(testUser);

        testUserMapper.delete("testUser");
        assertNull(testUserMapper.findById("testUser"));
    }
}

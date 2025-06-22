package com.fast_campus_12.not_found.shop.dao;

import com.fast_campus_12.not_found.shop.entity.User;
import com.fast_campus_12.not_found.shop.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserDAO {
    @Autowired
    private UserMapper userMapper;

//    public int countByUserId(String userId) {
//        log.debug("userId 중복 확인 요청됨: {}", userMapper.countByUserId(userId));
//        return userMapper.countByUserId(userId);
//    }

//    public int countByEmail(String email) {
//        return userMapper.countByEmail(email);
//    }

    public boolean existsByUserId(String userId) {
        boolean exists = userMapper.existsByUserId(userId); // 있으면 true -> 중복 아이디 있는거임
        log.debug("userId 중복 확인 - userId: {}, exists: {}", userId, exists);
        return exists;
    }

    public boolean existsByEmail(String email) {
        boolean exists = userMapper.existsByEmail(email);
        log.debug("email 중복 확인 - email: {}, exists: {}", email, exists);
        return exists;
    }

    public String insertUser(User user) {
        userMapper.insertUser(user);
        return user.getUserId();
    }

    public User selectByUserId(String userId) {
        return userMapper.selectByUserId(userId);
    }

    public User selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    public int deleteUser(Long id) {
        return userMapper.deleteUser(id);
    }

    public int updateUserStatus(Long id, Boolean isActive) {
        return userMapper.updateUserStatus(id, isActive);
    }

}
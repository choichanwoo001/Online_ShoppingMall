package com.fast_campus_12.not_found.shop.dao;

import com.fast_campus_12.not_found.shop.entity.User;
import com.fast_campus_12.not_found.shop.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    private UserMapper userMapper;

    public int countByUserId(String userId) {
        log.debug("userId 중복 확인 요청됨: {}", userMapper.countByUserId(userId)); // 로그 레벨 설정 가능
        return userMapper.countByUserId(userId);
    }

    public int countByEmail(String email) {
        return userMapper.countByEmail(email);
    }

    public Long insertUser(User user) {
        userMapper.insertUser(user);
        return user.getId();
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
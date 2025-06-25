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

    /**
     * LOGIN_ID 중복 확인 (사용자가 입력하는 아이디)
     */
    public boolean existsByLoginId(String loginId) {
        boolean exists = userMapper.existsByLoginId(loginId);
        log.debug("LOGIN_ID 중복 확인 - loginId: {}, exists: {}", loginId, exists);
        return exists;
    }

    /**
     * 사용자 등록
     */
    public Long insertUser(User user) {
        userMapper.insertUser(user);
        return user.getUserId(); // AUTO_INCREMENT로 생성된 PK 반환
    }

    /**
     * LOGIN_ID로 사용자 조회
     */
    public User findByLoginId(String loginId) {
        return userMapper.findByLoginId(loginId);
    }

    /**
     * 이메일로 사용자 조회 (UserDetail 테이블에서)
     */
    public User selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    /**
     * 사용자 정보 수정
     */
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 비밀번호 업데이트
     */
    public int updatePassword(String loginId, String hashedPassword) {
        return userMapper.updatePassword(loginId, hashedPassword);
    }
}
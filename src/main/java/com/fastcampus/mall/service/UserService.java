package com.fastcampus.mall.service;

import com.fastcampus.mall.entity.User;
import com.fastcampus.mall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 모든 사용자 조회
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * ID로 사용자 조회
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 사용자명으로 조회
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 사용자 생성
     */
    @Transactional
    public User createUser(User user) {
        // 중복 체크
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    /**
     * 사용자 수정
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setIsActive(userDetails.getIsActive());

        return userRepository.save(user);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }

        userRepository.deleteById(id);
    }

    /**
     * 활성 사용자 목록
     */
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
}
package com.fastcampus.mall.repository;

import com.fastcampus.mall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자명으로 찾기
    Optional<User> findByUsername(String username);

    // 이메일로 찾기
    Optional<User> findByEmail(String email);

    // 활성 사용자 목록
    List<User> findByIsActiveTrue();

    // 사용자명 존재 체크
    boolean existsByUsername(String username);

    // 이메일 존재 체크
    boolean existsByEmail(String email);

    // 사용자명 또는 이메일로 검색
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> findByUsernameOrEmailContaining(@Param("keyword") String keyword);
}
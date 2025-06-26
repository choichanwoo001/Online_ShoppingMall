package com.fast_campus_12.not_found.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestLoginController {

    /**
     * 테스트용 로그인 - userId로 강제 로그인
     */
    @GetMapping("/api/test/login/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testLogin(
            @PathVariable("userId") Long userId,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 세션에 로그인 정보 설정
            session.setAttribute("userId", userId);
            session.setAttribute("userRole", "USER");

            log.info("테스트 로그인 설정 완료: USER_ID={}", userId);

            response.put("success", true);
            response.put("message", "테스트 로그인이 설정되었습니다.");
            response.put("userId", userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("테스트 로그인 설정 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "테스트 로그인 설정에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 테스트용 로그아웃
     */
    @GetMapping("/api/test/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testLogout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = (Long) session.getAttribute("userId");

            // 세션 클리어
            session.removeAttribute("userId");
            session.removeAttribute("userRole");

            log.info("테스트 로그아웃 완료: USER_ID={}", userId);

            response.put("success", true);
            response.put("message", "테스트 로그아웃이 완료되었습니다.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("테스트 로그아웃 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "테스트 로그아웃에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 현재 세션 상태 확인
     */
    @GetMapping("/api/test/session")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long userId = (Long) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        response.put("success", true);
        response.put("userId", userId);
        response.put("userRole", userRole);
        response.put("isLoggedIn", userId != null);

        return ResponseEntity.ok(response);
    }
}
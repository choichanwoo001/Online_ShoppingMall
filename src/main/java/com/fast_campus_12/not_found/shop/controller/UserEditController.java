package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.UserUpdateRequest;
import com.fast_campus_12.not_found.shop.dto.UserInfoResponse;
import com.fast_campus_12.not_found.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserEditController {

    private final UserService userService;

    /**
     * 회원정보 수정 페이지 표시
     */
    @GetMapping("/user/{pageName}")
    public String userEditPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("title", "회원정보-수정");
        model.addAttribute("contentPath", "signup/" + pageName); // signup/basic 등
        return "layout/base";
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/api/user/current")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentUserInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // ★ 임시 테스트용 LOGIN_ID (실제로는 세션에서 가져와야 함)
            String loginId = "Choi3495";
            session.setAttribute("loginId", loginId); // 세션에 LOGIN_ID 저장

            // 실제 운영에서는 아래 코드 사용
            // String loginId = (String) session.getAttribute("loginId");
            // if (loginId == null) {
            //     response.put("success", false);
            //     response.put("message", "로그인이 필요합니다.");
            //     return ResponseEntity.status(401).body(response);
            // }

            log.debug("현재 사용자 정보 조회 요청: LOGIN_ID={}", loginId);

            // LOGIN_ID로 사용자 정보 조회
            UserInfoResponse userInfo = userService.getUserInfo(loginId);

            response.put("success", true);
            response.put("user", userInfo);

            log.debug("사용자 정보 조회 성공: LOGIN_ID={}", loginId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("사용자 정보 조회 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "사용자 정보 조회에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/api/user/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUserInfo(
            @RequestBody UserUpdateRequest request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // ★ 임시 테스트용 LOGIN_ID (실제로는 세션에서 가져와야 함)
            String sessionLoginId = "Choi3495";

            // 실제 운영에서는 아래 코드 사용
            // String sessionLoginId = (String) session.getAttribute("loginId");
            // if (sessionLoginId == null) {
            //     response.put("success", false);
            //     response.put("message", "로그인이 필요합니다.");
            //     return ResponseEntity.status(401).body(response);
            // }

            log.debug("회원정보 수정 요청: 세션 LOGIN_ID={}, 요청 LOGIN_ID={}",
                    sessionLoginId, request.getUserId());

            // 요청된 LOGIN_ID와 세션 LOGIN_ID 일치 확인
            if (!sessionLoginId.equals(request.getUserId())) {
                response.put("success", false);
                response.put("message", "권한이 없습니다.");
                return ResponseEntity.status(403).body(response);
            }

            // 유효성 검사 (내부적으로 LOGIN_ID → PK 변환도 수행됨)
            Map<String, String> errors = userService.validateUserUpdateRequest(request);
            if (!errors.isEmpty()) {
                response.put("success", false);
                response.put("message", "입력값 검증에 실패했습니다.");
                response.put("errors", errors);
                return ResponseEntity.badRequest().body(response);
            }

            log.debug("유효성 검사 통과");

            // 회원정보 수정
            boolean updateSuccess = userService.updateUserInfo(request);

            if (updateSuccess) {
                response.put("success", true);
                response.put("message", "회원정보가 성공적으로 수정되었습니다.");

                log.info("회원정보 수정 완료: LOGIN_ID={}", request.getUserId());

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "회원정보 수정에 실패했습니다.");
                return ResponseEntity.status(500).body(response);
            }

        } catch (Exception e) {
            log.error("회원정보 수정 실패: LOGIN_ID={}, 에러={}", request.getUserId(), e.getMessage(), e);
            response.put("success", false);
            response.put("message", "회원정보 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 사용자 존재 여부 확인 (관리자용)
     */
    @GetMapping("/api/user/exists/{loginId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUserExists(@PathVariable String loginId) {
        Map<String, Object> response = new HashMap<>();

        try {
            UserInfoResponse userInfo = userService.getUserInfo(loginId);

            response.put("success", true);
            response.put("exists", userInfo != null);
            response.put("user", userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.debug("사용자 조회 실패 (존재하지 않음): LOGIN_ID={}", loginId);
            response.put("success", true);
            response.put("exists", false);
            response.put("user", null);

            return ResponseEntity.ok(response);
        }
    }

    /**
     * 사용자 활성화/비활성화 (관리자용)
     */
    @PatchMapping("/api/user/{loginId}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleUserStatus(
            @PathVariable String loginId,
            @RequestBody Map<String, Boolean> request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 관리자 권한 확인 (실제로는 세션에서 관리자 여부 확인)
            // String adminRole = (String) session.getAttribute("userRole");
            // if (!"ADMIN".equals(adminRole)) {
            //     response.put("success", false);
            //     response.put("message", "관리자 권한이 필요합니다.");
            //     return ResponseEntity.status(403).body(response);
            // }

            Boolean isActive = request.get("isActive");
            if (isActive == null) {
                response.put("success", false);
                response.put("message", "활성화 상태 값이 필요합니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // LOGIN_ID로 사용자 조회하여 PK 획득
            UserInfoResponse userInfo = userService.getUserInfo(loginId);
            if (userInfo == null) {
                response.put("success", false);
                response.put("message", "사용자를 찾을 수 없습니다.");
                return ResponseEntity.status(404).body(response);
            }

            // 상태 변경 (실제 구현 시 UserService에 메서드 추가 필요)
            // userService.updateUserStatus(userInfo.getUserPkId(), isActive);

            response.put("success", true);
            response.put("message", "사용자 상태가 변경되었습니다.");

            log.info("사용자 상태 변경: LOGIN_ID={}, isActive={}", loginId, isActive);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("사용자 상태 변경 실패: LOGIN_ID={}, 에러={}", loginId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "사용자 상태 변경에 실패했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
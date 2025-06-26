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
             String loginId = (String) session.getAttribute("loginId");
             if (loginId == null) {
                 response.put("success", false);
                 response.put("message", "로그인이 필요합니다.");
                 return ResponseEntity.status(401).body(response);
             }

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
             String sessionLoginId = (String) session.getAttribute("loginId");
             if (sessionLoginId == null) {
                 response.put("success", false);
                 response.put("message", "로그인이 필요합니다.");
                 return ResponseEntity.status(401).body(response);
             }

            log.debug("회원정보 수정 요청: 세션 LOGIN_ID={}, 요청 LOGIN_ID={}",
                    sessionLoginId, request.getUserId());

            // 요청된 LOGIN_ID와 세션 LOGIN_ID 일치 확인
            if (!sessionLoginId.equals(request.getUserId())) {
                response.put("success", false);
                response.put("message", "권한이 없습니다.");
                return ResponseEntity.status(403).body(response);
            }

            // 유효성 검사
            Map<String, String> errors = userService.validateUserRequest(request, false);
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
}
>>>>>>> origin/develop

package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.ApiResponse;
import com.fast_campus_12.not_found.shop.service.UserService;
import com.fast_campus_12.not_found.shop.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FindAccountController {

    private final UserService userService;
    private final EmailService emailService;

    /**
     * 아이디 찾기 페이지
     */
    @GetMapping("/id/{pageName}")
    public String findIdPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("title", "아이디 찾기");
        model.addAttribute("contentPath", "auth/" + pageName);
        return "layout/base";
    }

    /**
     * 비밀번호 찾기 페이지
     */
    @GetMapping("/password/{pageName}")
    public String findPasswordPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("title", "비밀번호 찾기");
        model.addAttribute("contentPath", "auth/" + pageName);
        return "layout/base";
    }

    // 아이디 찾기 - 이메일 인증 요청
    @PostMapping("/api/find/id/send-verification")
    @ResponseBody
    public ResponseEntity<ApiResponse> sendIdVerification(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            // 이메일 유효성 검사
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "이메일을 입력해주세요.", null));
            }

            // 해당 이메일로 가입된 계정이 있는지 확인
            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "해당 이메일로 가입된 계정이 없습니다.", null));
            }

            // 이메일 인증코드 발송
            String verificationCode = emailService.sendVerificationEmail(email);
            if (verificationCode != null) {
                log.info("아이디 찾기 인증메일 발송 완료: email={}", email);
                return ResponseEntity.ok(
                        new ApiResponse(true, "인증메일이 발송되었습니다. 이메일을 확인해주세요.", null));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "이메일 발송에 실패했습니다.", null));
            }

        } catch (Exception e) {
            log.error("아이디 찾기 인증메일 발송 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }

    // 아이디 찾기 - 이메일 인증 확인 및 아이디 조회
    @PostMapping("/api/find/id/verify")
    @ResponseBody
    public ResponseEntity<ApiResponse> verifyAndFindId(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String code = request.get("code");

            // 이메일 인증 확인
            boolean isVerified = emailService.verifyEmailCode(email, code);
            if (!isVerified) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "인증코드가 일치하지 않습니다.", null));
            }

            // 인증 성공 시 아이디 조회
            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "해당 이메일로 가입된 계정이 없습니다.", null));
            }

            // 아이디를 마스킹 처리 (보안)
            String maskedLoginId = maskLoginId(loginId);

            Map<String, Object> data = new HashMap<>();
            data.put("loginId", maskedLoginId);
            data.put("fullLoginId", loginId); // 전체 아이디 (필요시)

            log.info("아이디 찾기 성공: email={}, loginId={}", email, maskedLoginId);
            return ResponseEntity.ok(
                    new ApiResponse(true, "아이디를 찾았습니다.", data));

        } catch (Exception e) {
            log.error("아이디 찾기 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }

    // 비밀번호 찾기 - 이메일 인증 요청
    @PostMapping("/api/find/password/send-verification")
    @ResponseBody
    public ResponseEntity<ApiResponse> sendPasswordVerification(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            // 이메일 유효성 검사
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "이메일을 입력해주세요.", null));
            }

            // 해당 이메일로 가입된 계정이 있는지 확인
            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "해당 이메일로 가입된 계정이 없습니다.", null));
            }

            // 이메일 인증코드 발송
            String verificationCode = emailService.sendVerificationEmail(email);
            if (verificationCode != null) {
                log.info("비밀번호 찾기 인증메일 발송 완료: email={}", email);
                return ResponseEntity.ok(
                        new ApiResponse(true, "인증메일이 발송되었습니다. 이메일을 확인해주세요.", null));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "이메일 발송에 실패했습니다.", null));
            }

        } catch (Exception e) {
            log.error("비밀번호 찾기 인증메일 발송 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }

    // 비밀번호 찾기 - 이메일 인증 확인 및 임시 비밀번호 발급
    @PostMapping("/api/find/password/verify")
    @ResponseBody
    public ResponseEntity<ApiResponse> verifyAndResetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String code = request.get("code");

            // 이메일 인증 확인
            boolean isVerified = emailService.verifyEmailCode(email, code);
            if (!isVerified) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "인증코드가 일치하지 않습니다.", null));
            }

            // 인증 성공 시 임시 비밀번호 생성 및 변경
            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "해당 이메일로 가입된 계정이 없습니다.", null));
            }

            // 임시 비밀번호 생성
            String tempPassword = generateTempPassword();

            // 비밀번호 변경 (임시 비밀번호로)
            boolean success = userService.resetPassword(loginId, tempPassword);
            if (!success) {
                return ResponseEntity.internalServerError()
                        .body(new ApiResponse(false, "비밀번호 재설정에 실패했습니다.", null));
            }

            // 임시 비밀번호를 이메일로 발송
            emailService.sendTempPasswordEmail(email, tempPassword);

            Map<String, Object> data = new HashMap<>();
            data.put("loginId", maskLoginId(loginId));
            data.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");

            log.info("비밀번호 재설정 성공: email={}, loginId={}", email, loginId);
            return ResponseEntity.ok(
                    new ApiResponse(true, "임시 비밀번호가 발급되었습니다. 이메일을 확인해주세요.", data));

        } catch (Exception e) {
            log.error("비밀번호 재설정 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }

    // 아이디 마스킹 처리
    private String maskLoginId(String loginId) {
        if (loginId == null || loginId.length() < 3) {
            return loginId;
        }

        int visibleChars = Math.min(3, loginId.length() / 2);
        String visible = loginId.substring(0, visibleChars);
        String masked = "*".repeat(loginId.length() - visibleChars);

        return visible + masked;
    }

    /**
     * 임시 비밀번호 생성
     */
    private String generateTempPassword() {
        // 임시 비밀번호 생성 로직 (영문+숫자 8자리)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            tempPassword.append(chars.charAt(index));
        }

        return tempPassword.toString();
    }
}
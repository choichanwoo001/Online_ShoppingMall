package com.fast_campus_12.not_found.shop.domain.auth.controller;

import com.fast_campus_12.not_found.shop.domain.user.service.UserService;
import com.fast_campus_12.not_found.shop.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FindAccountController {

    private final UserService userService;
    private final EmailService emailService;

    private static final String UPPER   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER   = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS  = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*";
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SYMBOLS;
    private static final SecureRandom RANDOM = new SecureRandom();

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

    /**
     * 아이디 찾기 - 이메일 인증 요청
     */
    @PostMapping("/api/find/login-id/send-verification")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendIdVerification(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");

            if (Objects.isNull(email) || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "이메일을 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                response.put("success", false);
                response.put("message", "해당 이메일로 가입된 계정이 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String verificationCode = emailService.sendVerificationEmail(email);
            if (verificationCode != null) {
                log.info("아이디 찾기 인증메일 발송 완료: email={}", email);
                response.put("success", true);
                response.put("message", "인증메일이 발송되었습니다. 이메일을 확인해주세요.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "이메일 발송에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            log.error("아이디 찾기 인증메일 발송 실패: {}", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 아이디 찾기 - 이메일 인증 확인 및 아이디 조회
     */
    @PostMapping("/api/find/login-id/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyAndFindId(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String code  = request.get("code");

            if (!emailService.verifyEmailCode(email, code)) {
                response.put("success", false);
                response.put("message", "인증코드가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                response.put("success", false);
                response.put("message", "해당 이메일로 가입된 계정이 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String maskedLoginId = maskLoginId(loginId);

            Map<String, String> data = new HashMap<>();
            data.put("loginId", maskedLoginId);
            data.put("fullLoginId", loginId);

            response.put("success", true);
            response.put("message", "아이디를 찾았습니다.");
            response.put("data", data);

            log.info("아이디 찾기 성공: email={}, loginId={}", email, maskedLoginId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("아이디 찾기 실패: {}", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 비밀번호 찾기 - 이메일 인증 요청
     */
    @PostMapping("/api/login-password/send-verification")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendPasswordVerification(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");

            if (Objects.isNull(email) || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "이메일을 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                response.put("success", false);
                response.put("message", "해당 이메일로 가입된 계정이 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String verificationCode = emailService.sendVerificationEmail(email);
            if (verificationCode != null) {
                log.info("비밀번호 찾기 인증메일 발송 완료: email={}", email);
                response.put("success", true);
                response.put("message", "인증메일이 발송되었습니다. 이메일을 확인해주세요.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "이메일 발송에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            log.error("비밀번호 찾기 인증메일 발송 실패: {}", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 비밀번호 찾기 - 이메일 인증 확인 및 임시 비밀번호 발급
     */
    @PostMapping("/api/find/password/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyAndResetPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String code  = request.get("code");

            if (!emailService.verifyEmailCode(email, code)) {
                response.put("success", false);
                response.put("message", "인증코드가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String loginId = userService.findLoginIdByEmail(email);
            if (loginId == null) {
                response.put("success", false);
                response.put("message", "해당 이메일로 가입된 계정이 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            String tempPassword = generateTempPassword();
            boolean success = userService.resetPassword(loginId, tempPassword);
            if (!success) {
                response.put("success", false);
                response.put("message", "비밀번호 재설정에 실패했습니다.");
                return ResponseEntity.status(500).body(response);
            }

            emailService.sendTempPasswordEmail(email, tempPassword);

            Map<String, String> data = new HashMap<>();
            data.put("tempPassword", tempPassword);                    // ← 추가
            data.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");

            response.put("success", true);
            response.put("message", "임시 비밀번호가 발급되었습니다.");
            response.put("data", data);

            log.info("비밀번호 재설정 성공: email={}, loginId={}", email, loginId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("비밀번호 재설정 실패: {}", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }


    /**
     * 아이디 마스킹 처리
     */
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
     * 대문자, 소문자, 숫자, 특수문자가 각각 1개 이상 포함된 임시 비밀번호 생성
     */
    private String generateTempPassword() {
        int length = 12; // 임시 비밀번호 길이

        List<Character> charList = new ArrayList<>(length);

        // 각 카테고리에서 최소 1개씩 반드시 포함
        charList.add(UPPER.charAt(RANDOM.nextInt(UPPER.length())));     // 대문자 1개
        charList.add(LOWER.charAt(RANDOM.nextInt(LOWER.length())));     // 소문자 1개
        charList.add(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));   // 숫자 1개
        charList.add(SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()))); // 특수문자 1개

        // 나머지 길이만큼 전체 문자 집합에서 랜덤 선택
        for (int i = 4; i < length; i++) {
            charList.add(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }

        // 문자 순서를 무작위로 섞음 (보안 강화)
        Collections.shuffle(charList, RANDOM);

        // List<Character>를 String으로 변환
        StringBuilder tempPassword = new StringBuilder(length);
        for (char c : charList) {
            tempPassword.append(c);
        }

        return tempPassword.toString();
    }

}

package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dao.UserDAO;
import com.fast_campus_12.not_found.shop.dto.SignupRequest;
import com.fast_campus_12.not_found.shop.dto.ApiResponse;
import com.fast_campus_12.not_found.shop.service.UserService;
import com.fast_campus_12.not_found.shop.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SignupController {
    private final UserService userService;
    private final EmailService emailService;
    private final UserDAO userDAO;

    /**
     * 회원가입 페이지 표시
     */
    @GetMapping("/signup/{pageName}")
    public String renderPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("title", "회원가입");
        model.addAttribute("contentPath", "signup/" + pageName); // signup/basic 등
        return "layout/base";
    }

    /**
     * 중복 아이디 확인 API
     */
    @RequestMapping(value = "/api/check-duplicate-id", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> checkDuplicateId(@RequestBody Map<String, String> request) {

        try {
            String userId = request.get("userId");  // 실제로는 LOGIN_ID

            // 입력값 디버깅
            log.debug("=== LOGIN_ID 중복확인 디버깅 시작 ===");
            log.debug("받은 loginId: [{}]", userId);
            log.debug("loginId 길이: {}", userId != null ? userId.length() : "null");

            // 유효성 검사
            if (userId == null || userId.trim().isEmpty()) {
                log.debug("❌ loginId가 비어있음");
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(false, "아이디를 입력해주세요.", null),
                        HttpStatus.BAD_REQUEST);
            }

            if (!UserService.USER_ID_PATTERN.matcher(userId).matches()) {
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(false, "영문+숫자 혼용 4~16자로 입력해주세요.", null),
                        HttpStatus.BAD_REQUEST);
            }
            log.debug("✅ 유효성 검사 통과");

            // LOGIN_ID 중복 확인
            boolean isAvailable = userService.isUserIdAvailable(userId);
            log.debug("DB 조회 결과 - 사용가능: {}", isAvailable);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("available", isAvailable);

            log.debug("=== LOGIN_ID 중복확인 성공 ===");
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(true, "조회 완료", data),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("❌ 중복확인 중 예외 발생", e);
            log.error("예외 타입: {}", e.getClass().getSimpleName());
            log.error("예외 메시지: {}", e.getMessage());
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(false, "서버 오류가 발생했습니다.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 이메일 인증코드 발송 API
     */
    @RequestMapping(value = "/api/send-email-verification", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> sendEmailVerification(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            // 이메일 유효성 검사
            if (email == null || email.trim().isEmpty()) {
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(false, "이메일을 입력해주세요.", null),
                        HttpStatus.BAD_REQUEST);
            }

            if (!UserService.EMAIL_PATTERN.matcher(email).matches()) {
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(false, "올바른 이메일 형식이 아닙니다.", null),
                        HttpStatus.BAD_REQUEST);
            }

            String verificationCode = emailService.sendVerificationEmail(email);

            if (verificationCode != null) {
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(true, "인증메일이 발송되었습니다.", null),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(false, "이메일 발송에 실패했습니다.", null),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(false, "이메일 발송 중 오류가 발생했습니다.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 이메일 인증코드 확인 API
     */
    @RequestMapping(value = "/api/verify-email", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        log.debug("=== 이메일 인증 확인 ===");
        log.debug("email: {}, code: {}", email, code);

        try {

            boolean isVerified = emailService.verifyEmailCode(email, code);
            log.debug("인증 결과: {}", isVerified);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("verified", isVerified);

            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(true, "인증 처리 완료", data),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(false, "인증 처리 중 오류가 발생했습니다.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원가입 처리 API
     */
    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest request) {
        try {
            // 서버 측 유효성 검사
            Map<String, String> validationErrors = userService.validateSignupRequest(request);
            if (!validationErrors.isEmpty()) {
                return new ResponseEntity<ApiResponse>(
                        new ApiResponse(false, "입력 정보를 확인해주세요.", validationErrors),
                        HttpStatus.BAD_REQUEST);
            }

            // 회원가입 처리 (PK 반환)
            Long userPkId = userService.signup(request);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("userId", request.getUserId());  // 화면에는 LOGIN_ID 반환
            data.put("userPkId", userPkId);          // 내부적으로는 PK도 반환

            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(true, "회원가입이 완료되었습니다.", data),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponse>(
                    new ApiResponse(false, "회원가입 중 오류가 발생했습니다.", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
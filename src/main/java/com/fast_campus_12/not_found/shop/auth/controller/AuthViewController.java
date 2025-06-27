package com.fast_campus_12.not_found.shop.auth.controller;

import com.fast_campus_12.not_found.shop.auth.dto.Auth;
import com.fast_campus_12.not_found.shop.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

    private final AuthService authService;

    /* 로그인 폼 화면 */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("contentPath", "login");
        return "layout/base";
    }

    /* 로그인 처리 */
    @PostMapping("/login")
    public String loginSubmit(
            @RequestParam("id") String id,
            @RequestParam("pw") String pw,
            Model model,
            HttpSession session
    ) {
        // 1. 입력값 유효성 검사
        if (Objects.isNull(id) || id.isBlank() || Objects.isNull(pw) || pw.isBlank()) {
            model.addAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
            model.addAttribute("contentPath", "login");
            return "layout/base";
        }

        // 2. 로그인 시도
        Auth user = authService.login(id, pw);
        if (Objects.nonNull(user)) {
            // 로그인 성공: 세션에 아이디 저장, 히스토리 기록
            session.getAttribute("loginId");
            session.setAttribute("loginId", user.getId());
            authService.uploadLoginHistory(id);
            return "redirect:/home";
        } else {
            // 로그인 실패: DB에 저장된 failCount 조회 후 모델에 전달
            Auth failedUser = authService.findById(id);
            int failCount = (Objects.nonNull(failedUser) ? failedUser.getFailCount() : 0);
            boolean locked = (Objects.nonNull(failedUser) && failedUser.isLocked());

            if (locked) {
                model.addAttribute("error", "해당 계정은 로그인 3회 실패로 잠겼습니다. 10분 후 다시 시도해 주세요.");
            } else {
                model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
                model.addAttribute("failCount", failCount);
            }

            model.addAttribute("contentPath", "login");
            return "layout/base";
        }
    }

    /* 로그아웃 처리 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
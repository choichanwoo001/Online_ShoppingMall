package com.fast_campus_12.not_found.shop.auth.controller;

import com.fast_campus_12.not_found.shop.auth.dto.Auth;
import com.fast_campus_12.not_found.shop.auth.repository.AuthRepository;
import com.fast_campus_12.not_found.shop.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Objects;


@Controller
public class AuthViewController {
    private final AuthService authService;
    private final AuthRepository authRepository;

    public AuthViewController(AuthService authService, AuthRepository authRepository) {
        this.authService = authService;
        this.authRepository = authRepository;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("contentPath", "login");
        return "layout/base";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam("id") String id, @RequestParam("pw") String pw, Model model, HttpSession session) {

        // 1. id, pw 유효성 검사
        if (Objects.isNull(id) || id.isBlank() || Objects.isNull(pw) || pw.isBlank()) {
            model.addAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
            model.addAttribute("contentPath", "login");
            return "layout/base";
        }

        // 2. 사용자 존재 여부 확인
        Auth auth = authRepository.findById(id);
        if (auth == null) {
            model.addAttribute("error", "존재하지 않는 계정입니다.");
            model.addAttribute("contentPath", "login");
            return "layout/base";
        }

        // 3. 계정 잠김 여부 확인
        if (auth.isLocked()) {
            model.addAttribute("error", "해당 계정은 로그인 시도 3회 실패로 잠겼습니다. 관리자에게 문의하세요.");
            model.addAttribute("contentPath", "login");
            return "layout/base";
        }

        // 4. 로그인 시도
        Auth updatedAuth = authService.login(id, pw);

        if (updatedAuth != null) {
            session.setAttribute("loginId", updatedAuth.getId());
            authService.uploadLoginHistory(updatedAuth.getId());
            return "redirect:/home";

        } else {
            // 로그인 실패 → DB에 저장된 failCount 기반으로 사용
            auth = authRepository.findById(id);
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            model.addAttribute("failCount", auth != null ? auth.getFailCount() : 0);
            model.addAttribute("contentPath", "login");
            return "layout/base";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

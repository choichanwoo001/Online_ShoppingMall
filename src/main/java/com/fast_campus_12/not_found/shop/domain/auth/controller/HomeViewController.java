package com.fast_campus_12.not_found.shop.domain.auth.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.Objects;

@Controller
public class HomeViewController {

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        if (Objects.isNull(loginId)) {
            return "redirect:/login";
        }

        model.addAttribute("loginId", loginId);
        model.addAttribute("contentPath", "home");
        return "layout/base";
    }
}

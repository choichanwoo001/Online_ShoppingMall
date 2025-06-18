package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model) {
        System.out.println("!");
        model.addAttribute("name", "Thymeleaf");
        return "hello"; // hello.html 템플릿을 렌더링
    }
}

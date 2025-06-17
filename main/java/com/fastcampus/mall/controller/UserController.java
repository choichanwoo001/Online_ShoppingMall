package com.fastcampus.mall.controller;

import com.fastcampus.mall.entity.User;
import com.fastcampus.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 사용자 목록 페이지
     */
    @GetMapping
    public String userList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/userList";
    }

    /**
     * 사용자 상세 페이지
     */
    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/userDetail";
        } else {
            return "redirect:/users";
        }
    }

    /**
     * 사용자 생성 폼
     */
    @GetMapping("/new")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "user/userForm";
    }

    /**
     * 사용자 생성 처리
     */
    @PostMapping
    public String createUser(@ModelAttribute User user) {
        try {
            userService.createUser(user);
            return "redirect:/users";
        } catch (Exception e) {
            return "redirect:/users/new?error=" + e.getMessage();
        }
    }

    // ===== REST API =====

    /**
     * 사용자 목록 API
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<User>> getUsersApi() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 조회 API
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserApi(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 사용자 생성 API
     */
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<User> createUserApi(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
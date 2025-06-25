package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.*;
import com.fast_campus_12.not_found.shop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/wishlist/{pageName}")
    public String renderPage(@PathVariable("pageName") String pageName, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
//        if (userId == null) {
//            return "redirect:/login"; // 로그인 안 했을 때 리디렉션
//        }

        // 필요한 데이터를 모델에 담아서 전달
        model.addAttribute("userId", userId);
        model.addAttribute("title", "위시리스트");
        model.addAttribute("contentPath", "myshop/" + pageName); // signup/basic 등
        return "layout/base";
    }

    @GetMapping("/api/wishlist")
    @ResponseBody
    public ResponseEntity<WishlistPageDto> getWishlist(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(WishlistPageDto.builder()
                    .success(false)
                    .message("로그인이 필요합니다.")
                    .build());
        }

        WishlistPageDto result = wishlistService.getWishlistByUserId(userId, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/wishlist/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToWishlist(
            @RequestParam Long productId,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        boolean success = wishlistService.addToWishlist(userId, productId);
        if (success) {
            response.put("success", true);
            response.put("message", "관심상품에 추가되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "이미 관심상품에 등록된 상품입니다.");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/api/wishlist/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromWishlist(
            @RequestParam Long productId,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        boolean success = wishlistService.removeFromWishlist(userId, productId);
        if (success) {
            response.put("success", true);
            response.put("message", "관심상품에서 제거되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "관심상품에서 제거하는데 실패했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/api/wishlist/delete-multiple")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMultiple(
            @RequestBody WishlistRequestDto request,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        boolean success = wishlistService.removeMultipleFromWishlist(request.getWishIds());
        if (success) {
            response.put("success", true);
            response.put("message", "선택한 상품들이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "상품 삭제에 실패했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/api/wishlist/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearWishlist(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }

        boolean success = wishlistService.clearWishlist(userId);
        if (success) {
            response.put("success", true);
            response.put("message", "모든 관심상품이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "관심상품 삭제에 실패했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/api/wishlist/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkWishlist(
            @RequestParam Long productId,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            response.put("success", true);
            response.put("data", false);
            return ResponseEntity.ok(response);
        }

        boolean isInWishlist = wishlistService.isInWishlist(userId, productId);
        response.put("success", true);
        response.put("data", isInWishlist);
        return ResponseEntity.ok(response);
    }
}
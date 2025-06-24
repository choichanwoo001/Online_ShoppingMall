package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.*;
import com.fast_campus_12.not_found.shop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/wishlist/{pageName}")
    public String renderPage(@PathVariable("pageName") String pageName, HttpSession session, Model model) {
//        String userId = (String) session.getAttribute("userId");
//        if (userId == null) {
//            return "redirect:/login"; // 로그인 안 했을 때 리디렉션
//        }

        // 필요한 데이터를 모델에 담아서 전달
//        model.addAttribute("userId", userId);
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
    public ResponseEntity<ApiResponse<Void>> addToWishlist(
            @RequestParam Long productId,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.error("로그인이 필요합니다."));
        }

        boolean success = wishlistService.addToWishlist(userId, productId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("관심상품에 추가되었습니다."));
        } else {
            return ResponseEntity.ok(ApiResponse.error("이미 관심상품에 등록된 상품입니다."));
        }
    }

    @GetMapping("/api/wishlist/remove")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @RequestParam Long productId,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.error("로그인이 필요합니다."));
        }

        boolean success = wishlistService.removeFromWishlist(userId, productId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("관심상품에서 제거되었습니다."));
        } else {
            return ResponseEntity.ok(ApiResponse.error("관심상품에서 제거하는데 실패했습니다."));
        }
    }

    @GetMapping("/api/wishlist/delete-multiple")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteMultiple(
            @RequestBody WishlistRequestDto request,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.error("로그인이 필요합니다."));
        }

        boolean success = wishlistService.removeMultipleFromWishlist(request.getWishIds());
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("선택한 상품들이 삭제되었습니다."));
        } else {
            return ResponseEntity.ok(ApiResponse.error("상품 삭제에 실패했습니다."));
        }
    }

    @GetMapping("/api/wishlist/clear")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> clearWishlist(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.error("로그인이 필요합니다."));
        }

        boolean success = wishlistService.clearWishlist(userId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("모든 관심상품이 삭제되었습니다."));
        } else {
            return ResponseEntity.ok(ApiResponse.error("관심상품 삭제에 실패했습니다."));
        }
    }

    @GetMapping("/api/wishlist/check")
    @ResponseBody
    public ResponseEntity<ApiResponse<Boolean>> checkWishlist(
            @RequestParam Long productId,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.success(false));
        }

        boolean isInWishlist = wishlistService.isInWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(isInWishlist));
    }
}
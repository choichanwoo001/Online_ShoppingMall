package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.coupon.*;
import com.fast_campus_12.not_found.shop.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/page/{pageName}")
    public String mypageDynamicPage(@PathVariable("pageName") String pageName,
                                    Model model,
                                    HttpSession session) {
        // 세션에서 사용자 ID 가져오기
        String loginId = (String) session.getAttribute("loginId");
//        Long userId = 1L;
        if (loginId == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        switch (pageName) {
            case "mypage":
                model.addAttribute("pageTitle", "마이 페이지");
                break;

            case "coupon":
                List<MyPageUserCouponDto> userCoupons = myPageService.getUserCoupons(loginId);
                MyPageCouponStatsDto couponStats = myPageService.getCouponStats(loginId);

                model.addAttribute("userCoupons", userCoupons);
                model.addAttribute("couponStats", couponStats);
                model.addAttribute("pageTitle", "쿠폰내역");
                model.addAttribute("pageSubtitle", "보유한 쿠폰을 확인하실 수 있습니다.");
                break;

            default:
                model.addAttribute("pageTitle", "마이페이지");
                model.addAttribute("pageSubtitle", "잘못된 접근입니다.");
                break;
        }

        model.addAttribute("currentPage", pageName);
        model.addAttribute("contentPath", "myshop/" + pageName);
        return "layout/base";
    }

    // 쿠폰 등록
    @PostMapping("/coupons/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerCoupon(@RequestBody MyPageCouponRegisterRequest request,
                                                              HttpSession session) {
        try {
            // 세션에서 사용자 ID 가져오기
            String loginId = (String) session.getAttribute("loginId");
//            Long userId = 1L;
            if (loginId == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "로그인이 필요합니다."
                ));
            }

            MyPageCouponRegisterDto result = myPageService.registerCoupon(loginId, request.getCouponCode());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "쿠폰이 성공적으로 등록되었습니다.",
                    "coupon", result
            ));
        } catch (IllegalArgumentException e) {
            log.warn("쿠폰 등록 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("쿠폰 등록 중 오류 발생", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "쿠폰 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            ));
        }
    }
}
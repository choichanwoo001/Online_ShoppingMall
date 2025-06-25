package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.dto.WishlistPageDto;
import com.fast_campus_12.not_found.shop.mapper.CartItemMapper;
import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.Cart;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import com.fast_campus_12.not_found.shop.product.service.CartService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/order/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private final CartService cartServiceImpl;

    //cart url 매핑 임시 + 팝업
    @GetMapping
    public String showPopup(HttpServletRequest request, Model model) {
        // 1. 사용자 ID 가져오기 (세션 또는 인증에서)
        //Long userId = getUserIdFromSession(request);
        Long userId = 3L;
        Cart cart = cartServiceImpl.getOrCreateCart(userId);
        // 장바구니와 장바구니 아이템들을 함께 조회
        Cart cartWithItems = cartServiceImpl.getCartWithItems(userId);

        // 장바구니 아이템 조회 (Thymeleaf에서 사용)
        List<CartItemViewDto> cartItems = cartServiceImpl.getCartItemViews(userId);
        // 요약 정보 계산
        Map<String, Object> summary = calculateCartSummary(cartItems);

        // getCartWithItems가 null을 반환하면 빈 장바구니 사용
        if (Objects.isNull(cartWithItems)) {
            cartWithItems = cart; // 방금 생성하거나 조회한 장바구니 사용
        }
        model.addAttribute("cart", cartWithItems);
        model.addAttribute("cartId", cartWithItems.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalItemCount", summary.get("totalItemCount"));
        model.addAttribute("totalAmount", summary.get("totalAmount"));
        model.addAttribute("originalTotalAmount", summary.get("originalTotalAmount"));
        model.addAttribute("totalDiscountAmount", summary.get("totalDiscountAmount"));
        model.addAttribute("contentPath", "order/cart");

        log.info("장바구니 조회 완료 - userId: {}, cartId: {}", userId, cartWithItems.getId());

        return "layout/base";
    }
    // 요약 정보 계산 메서드 (중복 코드 제거)
    private Map<String, Object> calculateCartSummary(List<CartItemViewDto> cartItems) {
        Map<String, Object> summary = new HashMap<>();

        if (Objects.nonNull(cartItems) && !cartItems.isEmpty()) {
            int totalItemCount = cartItems.size();

            // 총 금액 계산 (할인가 기준)
            int totalAmount = cartItems.stream()
                    .mapToInt(item -> item.getDiscountPrice() * item.getQuantity())
                    .sum();

            // 원래 가격 총합 (할인 전)
            int originalTotalAmount = cartItems.stream()
                    .mapToInt(item -> item.getPrice() * item.getQuantity())
                    .sum();

            // 총 할인 금액
            int totalDiscountAmount = originalTotalAmount - totalAmount;

            summary.put("totalItemCount", totalItemCount);
            summary.put("totalAmount", totalAmount);
            summary.put("originalTotalAmount", originalTotalAmount);
            summary.put("totalDiscountAmount", totalDiscountAmount);
        } else {
            summary.put("totalItemCount", 0);
            summary.put("totalAmount", 0);
            summary.put("originalTotalAmount", 0);
            summary.put("totalDiscountAmount", 0);
        }

        return summary;
    }

    // 장바구니 요약 정보 조회 API (총 금액, 아이템 수 등)
    @GetMapping("/summary")
    @ResponseBody
    public ResponseEntity<?> getCartSummary(HttpServletRequest request) {
        try {
            Long userId = 3L; // 실제로는 세션에서 가져와야 함

            // CartService를 통해 장바구니 아이템 조회
            List<CartItemViewDto> cartItemViewDtos = cartServiceImpl.getCartItemViews(userId);

            // 공통 메서드 사용
            Map<String, Object> summary = calculateCartSummary(cartItemViewDtos);

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            log.error("장바구니 요약 정보 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("장바구니 요약 정보 조회 중 오류가 발생했습니다.");
        }
    }
    
    // 장바구니 아이템 리스트만 조회하는 API (AJAX용)
    @GetMapping("/items")
    @ResponseBody
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            Long userId = 3L; // 실제로는 세션에서 가져와야 함

            // 먼저 장바구니 존재 확인 및 생성
            Cart cart = cartServiceImpl.getOrCreateCart(userId);

            // CartItemMapper를 사용해서 장바구니 아이템 뷰 정보 직접 조회
            List<CartItemViewDto> cartItemViewDtos = cartServiceImpl.getCartItemViews(userId);

            if (cartItemViewDtos == null || cartItemViewDtos.isEmpty()) {
                return ResponseEntity.ok().body(Collections.emptyList());
            }

            // 개발 환경에서 디버그 정보 포함
            Map<String, Object> response = new HashMap<>();
            response.put("data", cartItemViewDtos);
            response.put("debug", Map.of(
                    "userId", userId,
                    "itemCount", cartItemViewDtos.size(),
                    "timestamp", System.currentTimeMillis()
            ));

            log.info("장바구니 아이템 조회 완료 - userId: {}, 아이템 수: {}", userId, cartItemViewDtos.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("장바구니 아이템 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("장바구니 조회 중 오류가 발생했습니다.");
        }
    }

    // 장바구니 아이템 삭제 API
    @DeleteMapping("/items/{cartItemId}")
    @ResponseBody
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartItemId") String cartItemId, HttpServletRequest request) {
        try {
            Long userId = 3L; // 실제로는 세션에서 가져와야 함

            // 장바구니 아이템 삭제
            cartServiceImpl.removeItemFromCart(userId, cartItemId);

            log.info("장바구니 아이템 삭제 완료 - userId: {}, cartItemId: {}", userId, cartItemId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "상품이 장바구니에서 삭제되었습니다.",
                    "deletedItemId", cartItemId
            ));

        } catch (IllegalArgumentException e) {// 실제로는 세션에서 가져와야 함
            Long userId = 3L;
            log.info("장바구니 아이템 삭제 실패 - 장바구니를 찾을 수 없음: userId= {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            log.error("장바구니 아이템 삭제 중 오류 발생 - cartItemId: {}", cartItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "상품 삭제 중 오류가 발생했습니다.",
                            "error", e.getMessage()
                    ));
        }
    }
}

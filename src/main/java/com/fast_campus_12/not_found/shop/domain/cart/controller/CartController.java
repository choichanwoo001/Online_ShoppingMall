package com.fast_campus_12.not_found.shop.domain.cart.controller;

import com.fast_campus_12.not_found.shop.domain.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.domain.product.model.Cart;
import com.fast_campus_12.not_found.shop.domain.product.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/order/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private final CartService cartServiceImpl;

    /**
     * 세션 기반 사용자 ID 생성 (간단 버전)
     */
    private Long getEffectiveUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // 1. 로그인 사용자 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            return userId;
        }

        // 2. 비로그인 사용자 - 세션 해시값으로 임시 ID 생성
        if (userId == null) {
            userId = 11L; // 테스트용 사용자 ID
            session.setAttribute("userId", userId);
            session.setAttribute("userName", "테스트사용자");
            session.setAttribute("loginId", "user007");
            session.setAttribute("isLoggedIn", true);
        }

        return userId;
    }

    //cart url 매핑 임시 + 팝업
    @GetMapping
    public String showPopup(HttpServletRequest request, HttpSession session,Model model) {
        // 1. 사용자 ID 가져오기 (세션 또는 인증에서)
        Long userId = getEffectiveUserId(request);
        Cart cart = cartServiceImpl.getOrCreateCart(userId);
        Cart cartWithItems = cartServiceImpl.getCartWithItems(userId);
        List<CartItemViewDto> cartItems = cartServiceImpl.getCartItemViews(userId);

        // getCartWithItems가 null을 반환하면 빈 장바구니 사용
        if (Objects.isNull(cartWithItems)) {
            cartWithItems = cart; // 방금 생성하거나 조회한 장바구니 사용
        }
        model.addAttribute("cart", cartWithItems);
        model.addAttribute("userId", userId);
        model.addAttribute("cartItems", cartItems);
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
            int totalAmount = 0;

            // 원래 가격 총합 (할인 전)
            int originalTotalAmount = 0;

            for (CartItemViewDto item : cartItems) {
                totalAmount += item.getDiscountPrice() * item.getQuantity();
                originalTotalAmount += item.getPrice() * item.getQuantity();
            }

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
    @GetMapping("/api/summary")
    @ResponseBody
    public ResponseEntity<?> getCartSummary(HttpServletRequest request) {
        try {
            Long userId = getEffectiveUserId(request);

            // CartService의 calculateCartSummary 메서드 사용
            Map<String, Object> summary = cartServiceImpl.calculateCartSummary(userId);

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            log.error("장바구니 요약 정보 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "장바구니 조회 중 오류가 발생했습니다."));
        }
    }

    // 장바구니 아이템 리스트 조회 API
    @GetMapping("/api/items")
    @ResponseBody
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            Long userId = getEffectiveUserId(request);

            List<CartItemViewDto> cartItemViewDtos = cartServiceImpl.getCartItemViews(userId);

            if (Objects.isNull(cartItemViewDtos) || cartItemViewDtos.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            log.info("장바구니 아이템 조회 완료 - userId: {}, 아이템 수: {}", userId, cartItemViewDtos.size());
            return ResponseEntity.ok(cartItemViewDtos);

        } catch (Exception e) {
            log.error("장바구니 아이템 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "장바구니 조회 중 오류가 발생했습니다."));
        }
    }

    // 장바구니에 상품 추가 API
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addItemToCart(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
        try {
            Long userId = getEffectiveUserId(httpRequest);
            Long productVariantId = Long.valueOf(request.get("productVariantId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());

            // 유효성 검증
            if (productVariantId == null || quantity == null || quantity < 1) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "잘못된 요청 데이터입니다."));
            }

            if (quantity > 99) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "최대 99개까지 주문 가능합니다."));
            }

            // === 이미 담겨져 있는지 먼저 확인 ===
            List<CartItemViewDto> existingItems = cartServiceImpl.getCartItemViews(userId);

            boolean isAlreadyInCart = existingItems.stream()
                    .anyMatch(item -> item.getProductVariantId().equals(productVariantId));

            if (isAlreadyInCart) {
                // 이미 담겨져 있으면 추가하지 않고 메시지만 반환
                log.info("장바구니 추가 시도 - 이미 존재하는 상품: userId={}, productVariantId={}", userId, productVariantId);

                return ResponseEntity.ok(Map.of(
                        "productVariantId", productVariantId,
                        "isAlreadyInCart", true,
                        "message", "이미 장바구니에 담긴 상품입니다."
                ));
            }

            // CartService의 기존 메서드 활용
            cartServiceImpl.addItemToCart(userId, productVariantId, quantity);

            log.info("장바구니 상품 추가 완료 - userId: {}, productVariantId: {}, quantity: {}",
                    userId, productVariantId, quantity);
            return ResponseEntity.ok(Map.of(
                    "productVariantId", productVariantId,
                    "quantity", quantity,
                    "isAlreadyInCart", false,
                    "message", "장바구니에 상품이 추가되었습니다."
            ));

        } catch (Exception e) {
            log.error("장바구니 상품 추가 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "상품 추가 중 오류가 발생했습니다."));
        }
    }

    // 장바구니 아이템 삭제 API
    @DeleteMapping("/api/items")
    @ResponseBody
    public ResponseEntity<?> deleteCartItems(@RequestBody List<String> cartItemIds, HttpServletRequest request) {
        try {
            Long userId = getEffectiveUserId(request);

            if (cartItemIds == null || cartItemIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "삭제할 상품을 선택해주세요."));
            }

            // 각 아이템 삭제
            for (String cartItemId : cartItemIds) {
                cartServiceImpl.removeItemFromCart(userId, cartItemId);
            }

            log.info("장바구니 아이템 삭제 완료 - userId: {}, 삭제된 아이템 수: {}, 아이템 IDs: {}",
                    userId, cartItemIds.size(), cartItemIds);

            return ResponseEntity.ok().build(); // 성공 시 빈 응답

        } catch (IllegalArgumentException e) {
            log.info("장바구니 아이템 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("장바구니 아이템 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "상품 삭제 중 오류가 발생했습니다."));
        }
    }

    // 장바구니 상태 확인 API (비어있는지, 아이템 개수 등)
    @GetMapping("/api/status")
    @ResponseBody
    public ResponseEntity<?> getCartStatus(HttpServletRequest request) {
        try {
            Long userId = getEffectiveUserId(request);

            List<CartItemViewDto> cartItems = cartServiceImpl.getCartItemViews(userId);

            boolean isEmpty = cartItems == null || cartItems.isEmpty();
            int itemCount = isEmpty ? 0 : cartItems.size();

            Map<String, Object> status = Map.of(
                    "isEmpty", isEmpty,
                    "itemCount", itemCount
            );

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            log.error("장바구니 상태 확인 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "장바구니 상태 확인 중 오류가 발생했습니다."));
        }
    }

    // 장바구니 아이템 수량 변경 API
    @PatchMapping("/api/items/{cartItemId}/quantity")
    @ResponseBody
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable("cartItemId") String cartItemId,
            @RequestBody Map<String, Integer> request,
            HttpServletRequest httpRequest) {
        try {
            Long userId = getEffectiveUserId(httpRequest);
            Integer newQuantity = request.get("quantity");

            // 유효성 검증
            if (newQuantity == null || newQuantity < 1) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "수량은 1개 이상이어야 합니다."));
            }

            if (newQuantity > 99) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "최대 99개까지 주문 가능합니다."));
            }

            // 기존 CartService의 updateItemQuantity 메서드 활용!
            cartServiceImpl.updateItemQuantity(userId, cartItemId, newQuantity);

            // 성공 응답 (간단하게)
            Map<String, Object> response = Map.of(
                    "cartItemId", cartItemId,
                    "quantity", newQuantity
            );

            log.info("장바구니 수량 변경 완료 - userId: {}, cartItemId: {}, newQuantity: {}",
                    userId, cartItemId, newQuantity);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.info("장바구니 수량 변경 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("장바구니 수량 변경 중 오류 발생 - cartItemId: {}", cartItemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "수량 변경 중 오류가 발생했습니다."));
        }
    }
}

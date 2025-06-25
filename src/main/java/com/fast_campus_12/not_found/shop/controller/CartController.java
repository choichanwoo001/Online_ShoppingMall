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
    private CartService cartServiceImpl;

    //cart url 매핑 임시 + 팝업
    @GetMapping
    public String showPopup(HttpServletRequest request, Model model) {
        // 1. 사용자 ID 가져오기 (세션 또는 인증에서)
        //Long userId = getUserIdFromSession(request);
        Long userId = 5L;
        Cart cart = cartServiceImpl.getOrCreateCart(userId);
        // 장바구니와 장바구니 아이템들을 함께 조회
        Cart cartWithItems = cartServiceImpl.getCartWithItems(userId);

        // 장바구니 아이템 조회 (Thymeleaf에서 사용)
        List<CartItemViewDto> cartItems = cartServiceImpl.getCartItemViews(userId);


        // getCartWithItems가 null을 반환하면 빈 장바구니 사용
        if (cartWithItems == null) {
            cartWithItems = cart; // 방금 생성하거나 조회한 장바구니 사용
        }
        model.addAttribute("cart", cartWithItems);
        model.addAttribute("cartId", cartWithItems.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("contentPath", "order/cart");

        log.info("장바구니 조회 완료 - userId: {}, cartId: {}", userId, cartWithItems.getId());

        return "layout/base";
    }

    // 장바구니 아이템 리스트만 조회하는 API (AJAX용)
    @GetMapping("/items")
    @ResponseBody
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            Long userId = 4L; // 실제로는 세션에서 가져와야 함

            // 먼저 장바구니 존재 확인 및 생성
            Cart cart = cartServiceImpl.getOrCreateCart(userId);

            // CartItemMapper를 사용해서 장바구니 아이템 뷰 정보 직접 조회
            List<CartItemViewDto> cartItemViewDtos = cartServiceImpl.getCartItemViews(userId);

            if (Objects.isNull(cartItemViewDtos) || cartItemViewDtos.isEmpty()) {
                return ResponseEntity.ok().body(Collections.emptyList());
            }

            return ResponseEntity.ok(cartItemViewDtos);

        } catch (Exception e) {
            log.error("장바구니 아이템 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("장바구니 조회 중 오류가 발생했습니다.");
        }
    }
}

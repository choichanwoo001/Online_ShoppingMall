package com.fast_campus_12.not_found.shop.domain.product.service;

import com.fast_campus_12.not_found.shop.domain.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.domain.product.model.Cart;

import java.util.List;
import java.util.Map;

public interface CartService {
    //사용자 장바구니 조회
    Cart getOrCreateCart(Long userId);

    //장바구니에 상품 추가
    void addItemToCart(Long userId, Long productVariantId, int quantity);

    //장바구니 아이템 수량 변경
    void updateItemQuantity(Long userId, String cartItemId, int quantity);

    //장바구니 아이템 삭제
    void removeItemFromCart(Long userId, String cartItemId);

    //사용자의 장바구니 전체 조회(아이템 포함)
    Cart getCartWithItems(Long userId);


    //장바구니 아이디로 장바구니 목록 불러오기
    List<CartItemViewDto> getCartItemViews(Long userId);

    Map<String, Object> calculateCartSummary(Long userId);
}

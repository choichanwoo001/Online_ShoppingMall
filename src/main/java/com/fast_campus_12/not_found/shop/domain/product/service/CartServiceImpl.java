package com.fast_campus_12.not_found.shop.domain.product.service;

import com.fast_campus_12.not_found.shop.mapper.CartItemMapper;
import com.fast_campus_12.not_found.shop.mapper.CartMapper;
import com.fast_campus_12.not_found.shop.domain.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.domain.product.model.Cart;
import com.fast_campus_12.not_found.shop.domain.product.model.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;


    @Override
    public Cart getOrCreateCart(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (Objects.isNull(cart)) {
            cart = new Cart();
//            cart.setId(UUID.randomUUID().toString());
            cart.setUserId(userId);
            cartMapper.insertCart(cart);
            log.debug("장바구니 없음");
        }
        return cart;
    }

    @Override
    public void addItemToCart(Long userId, Long productVariantId, int quantity) {
        // 장바구니 조회/생성
        Cart cart = getOrCreateCart(userId);

        // 이미 있는 상품인지 확인
        CartItem existingItem = cartItemMapper.findByCartIdAndProductId(cart.getId(), productVariantId);

        if (Objects.nonNull(existingItem)) {
            // 기존 상품이면 수량 업데이트
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemMapper.updateQuantity(existingItem);

            log.debug("장바구니에 이미 담겨져있습니다.");
        } else {
            // 새 상품이면 추가
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductVariantId(productVariantId);
            newItem.setQuantity(quantity);
            cartItemMapper.insertCartItem(newItem);
        }

        // 장바구니 업데이트 시간 갱신
        cartMapper.updateCart(cart.getId());
    }

    @Override
    public void updateItemQuantity(Long userId, String cartItemId, int quantity) {
        Cart cart = cartMapper.findByUserId(userId);
        if (Objects.isNull(cart)) {
            throw new IllegalArgumentException("장바구니를 찾을 수 없습니다.");
        }

        if (quantity <= 0) {
            cartItemMapper.deleteCartItem(cartItemId);
        } else {
            CartItem item = new CartItem();
            item.setId(cartItemId);
            item.setCartId(cart.getId());
            item.setQuantity(quantity);
            cartItemMapper.updateQuantity(item);
        }

        cartMapper.updateCart(cart.getId());
    }

    @Override
    public void removeItemFromCart(Long userId, String cartItemId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (Objects.isNull(cart)) {
            throw new IllegalArgumentException("장바구니를 찾을 수 없습니다.");
        }

        cartItemMapper.deleteCartItem(cartItemId);
        cartMapper.updateCart(cart.getId());
    }

    @Override
    public Cart getCartWithItems(Long userId) {
        return cartMapper.findCartWithItemsByUserId(userId);
    }

    @Override
    public List<CartItemViewDto> getCartItemViews(Long userId) {
        // 먼저 장바구니가 있는지 확인하고 없으면 생성
        Cart cart = getOrCreateCart(userId);
        // 장바구니 아이템 뷰 조회
        return cartItemMapper.findCartItemViewsByCartId(cart.getId());
    }

    @Override
    public Map<String, Object> calculateCartSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();

        List<CartItemViewDto> cartItems = getCartItemViews(userId);

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
}

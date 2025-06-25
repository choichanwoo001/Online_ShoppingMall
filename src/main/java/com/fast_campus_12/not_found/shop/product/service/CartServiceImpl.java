package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.mapper.CartItemMapper;
import com.fast_campus_12.not_found.shop.mapper.CartMapper;
import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.Cart;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    // 생성자 주입 (더 안전한 방식)
    public CartServiceImpl(CartMapper cartMapper, CartItemMapper cartItemMapper) {
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public Cart getOrCreateCart(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
//            cart.setId(UUID.randomUUID().toString());
            cart.setUserId(userId);
            cartMapper.insertCart(cart);
            System.out.println("장바구니 없음");
        }
        return cart;
    }

    @Override
    public void addItemToCart(Long userId, Long productId, Long productVId, int quantity) {
        // 장바구니 조회/생성
        Cart cart = getOrCreateCart(userId);

        // 이미 있는 상품인지 확인
        CartItem existingItem = cartItemMapper.findByCartIdAndProductId(cart.getId(), productId);

        if (existingItem != null) {
            // 기존 상품이면 수량 업데이트
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemMapper.updateQuantity(existingItem);

            System.out.println("장바구니에 이미 담겨져있습니다.");
        } else {
            // 새 상품이면 추가
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductVId(productVId);
            newItem.setQuantity(quantity);
            cartItemMapper.insertCartItem(newItem);
        }

        // 장바구니 업데이트 시간 갱신
        cartMapper.updateCart(cart.getId());
    }

    @Override
    public void updateItemQuantity(Long userId, String cartItemId, int quantity) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) {
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
        if (cart == null) {
            throw new IllegalArgumentException("장바구니를 찾을 수 없습니다.");
        }

        cartItemMapper.deleteCartItem(cartItemId);
        cartMapper.updateCart(cart.getId());
    }

    //장바구니 전체 조회
    @Transactional(readOnly = true)
    @Override
    public Cart getCartWithItems(Long userId) {
        return cartMapper.findCartWithItemsByUserId(userId);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart != null) {
            cartItemMapper.deleteAllByCartId(cart.getId());
            cartMapper.updateCart(cart.getId());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public int getCartItemCount(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) {
            return 0;
        }
        return cartItemMapper.countByCartId(cart.getId());
    }

    @Override
    public List<CartItemViewDto> getCartItemViews(Long userId) {
        // 먼저 장바구니가 있는지 확인하고 없으면 생성
        Cart cart = getOrCreateCart(userId);
        // 장바구니 아이템 뷰 조회
        return cartItemMapper.findCartItemViewsByCartId(cart.getId());
    }
}

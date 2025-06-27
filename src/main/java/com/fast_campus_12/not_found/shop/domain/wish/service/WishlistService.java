package com.fast_campus_12.not_found.shop.domain.wish.service;

import com.fast_campus_12.not_found.shop.domain.wish.dto.WishlistItemDto;
import com.fast_campus_12.not_found.shop.domain.wish.dto.WishlistPageDto;
import com.fast_campus_12.not_found.shop.mapper.WishlistMapper;
//import com.fast_campus_12.not_found.shop.mapper.CartMapper;
import com.fast_campus_12.not_found.shop.domain.wish.dto.WishList;
//import com.fast_campus_12.not_found.shop.entity.Cart;
//import com.fast_campus_12.not_found.shop.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistMapper wishlistMapper;
//    private final CartMapper cartMapper;

    public WishlistPageDto getWishlistByUserId(String userId, int page, int size) {
        int offset = (page - 1) * size;

        List<WishlistItemDto> wishlistItems = wishlistMapper.selectWishlistByUserId(userId, offset, size);
        int totalCount = wishlistMapper.countWishlistByUserId(userId);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        return WishlistPageDto.builder()
                .content(wishlistItems)
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements((long) totalCount)
                .size(size)
                .success(true)
                .build();
    }

    public boolean addToWishlist(String userId, Long productId) {
        if (wishlistMapper.existsByUserIdAndProductId(userId, productId)) {
            return false; // 이미 관심상품에 있음
        }

        WishList wishList = WishList.builder()
                .userId(userId)
                .productId(productId)
                .build();

        return wishlistMapper.insertWishlist(wishList) > 0;
    }

    public boolean removeFromWishlist(String userId, Long productId) {
        return wishlistMapper.deleteByUserIdAndProductId(userId, productId) > 0;
    }

    @Transactional
    public boolean removeMultipleFromWishlist(List<Long> wishIds) {
        if (wishIds == null || wishIds.isEmpty()) {
            return false;
        }
        return wishlistMapper.deleteByWishIds(wishIds) > 0;
    }

    public boolean clearWishlist(String userId) {
        return wishlistMapper.deleteAllByUserId(userId) > 0;
    }

//    @Transactional
//    public boolean addToCartAndRemoveFromWishlist(String userId, List<Long> productIds, List<Long> wishIds) {
//        // 사용자의 장바구니 조회 또는 생성
//        Cart cart = cartMapper.selectCartByUserId(userId);
//        if (cart == null) {
//            cart = Cart.builder().userId(userId).build();
//            cartMapper.insertCart(cart);
//        }
//
//        // 상품들을 장바구니에 추가
//        for (Long productId : productIds) {
//            CartItem existingItem = cartMapper.selectCartItemByCartIdAndProductId(cart.getCartId(), productId);
//
//            if (existingItem != null) {
//                // 기존 아이템이 있으면 수량 증가
//                cartMapper.updateCartItemQuantity(existingItem.getCartItemId(), 1);
//            } else {
//                // 새 아이템 추가
//                CartItem newItem = CartItem.builder()
//                        .cartId(cart.getCartId())
//                        .productId(productId)
//                        .quantity(1)
//                        .build();
//                cartMapper.insertCartItem(newItem);
//            }
//        }
//
//        // 관심상품에서 제거
//        if (wishIds != null && !wishIds.isEmpty()) {
//            wishlistMapper.deleteByWishIds(wishIds);
//        }
//
//        return true;
//    }

    public boolean isInWishlist(String userId, Long productId) {
        return wishlistMapper.existsByUserIdAndProductId(userId, productId);
    }
}

package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.dto.*;
import com.fast_campus_12.not_found.shop.mapper.WishlistMapper;
import com.fast_campus_12.not_found.shop.entity.WishList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistMapper wishlistMapper;

    public WishlistPageDto getWishlistByUserId(Long userId, int page, int size) {
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

    public boolean addToWishlist(Long userId, Long productId) {
        if (wishlistMapper.existsByUserIdAndProductId(userId, productId)) {
            return false; // 이미 관심상품에 있음
        }

        WishList wishList = WishList.builder()
                .userId(String.valueOf(userId))
                .productId(productId)
                .build();

        return wishlistMapper.insertWishlist(wishList) > 0;
    }

    public boolean removeFromWishlist(Long userId, Long productId) {
        return wishlistMapper.deleteByUserIdAndProductId(userId, productId) > 0;
    }

    @Transactional
    public boolean removeMultipleFromWishlist(List<Long> wishIds) {
        if (wishIds == null || wishIds.isEmpty()) {
            return false;
        }
        return wishlistMapper.deleteByWishIds(wishIds) > 0;
    }

    public boolean clearWishlist(Long userId) {
        return wishlistMapper.deleteAllByUserId(userId) > 0;
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistMapper.existsByUserIdAndProductId(userId, productId);
    }
}
package com.fast_campus_12.not_found.shop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishList {
    private Long wishId;
    private String userId;
    private Long productId;
    private LocalDateTime addedAt;
}


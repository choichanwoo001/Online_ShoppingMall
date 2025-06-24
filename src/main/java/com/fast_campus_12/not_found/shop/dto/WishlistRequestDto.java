package com.fast_campus_12.not_found.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequestDto {
    private List<Long> wishIds;
    private List<Long> productIds;
    private Boolean removeFromWishlist;
}

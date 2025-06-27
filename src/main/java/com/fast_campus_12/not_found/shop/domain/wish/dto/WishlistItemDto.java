package com.fast_campus_12.not_found.shop.domain.wish.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItemDto {
    private Long wishId;
    private String userId;
    private Long productId;
    private String title;
    private String summary;
    private String thumbnail;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer stockQuantity;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private String tags;
    private String categoryName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime addedAt;
    private Boolean enabled;
}


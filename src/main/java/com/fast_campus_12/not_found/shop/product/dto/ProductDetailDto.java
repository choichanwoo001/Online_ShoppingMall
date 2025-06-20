package com.fast_campus_12.not_found.shop.product.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class ProductDetailDto {
    private String title;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String thumbnail;
    private String summary;
}
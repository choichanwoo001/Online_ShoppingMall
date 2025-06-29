package com.fast_campus_12.not_found.shop.domain.product.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewDto {
    private String username;
    private String createdDate;
    private String content;
    private int rating;
    private List<String> images;
}

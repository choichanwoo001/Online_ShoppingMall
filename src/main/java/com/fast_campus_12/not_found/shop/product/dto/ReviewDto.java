package com.fast_campus_12.not_found.shop.product.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDto {
    private String username;
    private String createdDate;
    private String content;
    public ReviewDto(String username, String createdDate, String content) {
        this.username = username;
        this.createdDate = createdDate;
        this.content = content;
    }
}

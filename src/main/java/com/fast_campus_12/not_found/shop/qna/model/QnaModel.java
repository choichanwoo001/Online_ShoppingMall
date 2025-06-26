package com.fast_campus_12.not_found.shop.qna.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QnaModel {
    private Long inquiryId;
    private Long userId;
    private Long productId;
    private String title;
    private String content;
    private String inquiryCategory;
    private String status;
    private boolean isSecret;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
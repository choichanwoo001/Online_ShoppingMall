package com.fast_campus_12.not_found.shop.inquiry.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryDto {
    private Long inquiryId;
    private Long userId;
    private Long productId;
    private String title;
    private String content;
    private InquiryCategory inquiryCategory;
    private InquiryStatus status;
    private boolean isSecret;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public enum InquiryCategory {
        PRODUCT, DELIVERY, PAYMENT, REFUND, ETC
    }

    public enum InquiryStatus {
        PENDING, ANSWERED, CLOSED
    }
}
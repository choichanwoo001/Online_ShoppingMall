package com.fast_campus_12.not_found.shop.inquiry.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {
    private Long id;
    private Long userId;
    private String writer;
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
package com.fast_campus_12.not_found.shop.inquiry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InquiryDetailDto {

    private Long inquiryId;
    private Long userId;
    private Long productId;

    private String title;
    private String content;
    private String inquiryCategory; // enum이면 Enum 타입으로 바꿔도 됨
    private String status;          // enum이면 Enum 타입으로 바꿔도 됨
    private boolean isSecret;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String writerName;
    private String writerEmail;

    private String productTitle;
    private BigDecimal productPrice;

    private String answerContent;
    private String adminName;
    private LocalDateTime answeredAt;
}

package com.fast_campus_12.not_found.shop.inquiry.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryAnswerDto {
    private Long inquiryAnswerId;
    private Long inquiryId;
    private String adminId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
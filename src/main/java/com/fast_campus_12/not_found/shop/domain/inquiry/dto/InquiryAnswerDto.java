package com.fast_campus_12.not_found.shop.domain.inquiry.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryAnswerDto {
    private Long inquiryAnswerId;
    private Long inquiryId;
    private String adminId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
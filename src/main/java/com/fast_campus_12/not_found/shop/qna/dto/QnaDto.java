package com.fast_campus_12.not_found.shop.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public class QnaDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private boolean isSecret;
    private String answer;  // 답변
}

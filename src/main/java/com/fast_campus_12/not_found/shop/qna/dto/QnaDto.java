package com.fast_campus_12.not_found.shop.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QnaDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String nickname;     // 추후 JOIN으로 붙일 경우
    private String content;
    private LocalDateTime createdAt;
    private boolean isSecret;
    private String answer;       // 답변 (추후 조인)
}

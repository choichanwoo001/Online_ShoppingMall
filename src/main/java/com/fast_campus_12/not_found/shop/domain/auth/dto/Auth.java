package com.fast_campus_12.not_found.shop.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
    private Long userId;
    private String id;
    private String password;
    private Boolean isActivate;
    private String role;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // 로그인 실패 관련 필드 (조회 시에만 사용)
    private int failCount = 0;
    // Helper methods
    private boolean locked = false;
}

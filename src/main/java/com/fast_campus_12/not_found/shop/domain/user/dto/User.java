package com.fast_campus_12.not_found.shop.domain.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long userId; // user_id (실제로는 email)
    private String loginId;
    private String password;        // 암호화된 비밀번호
    private Boolean isActivate;     // 활성화 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String role;            // 권한
    private Boolean isDeleted;      // 삭제 여부
}
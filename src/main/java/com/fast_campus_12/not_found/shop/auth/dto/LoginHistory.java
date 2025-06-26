package com.fast_campus_12.not_found.shop.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistory {
    private Long id;
    private Long userId;
    private String ip;
    private String browser;
    private String nation;
    private String region;
    private Boolean attemptResult;
    private Integer consecutiveFailedLoginAttempt;
    private Boolean isLocked;
    private LocalDateTime createdAt;
}

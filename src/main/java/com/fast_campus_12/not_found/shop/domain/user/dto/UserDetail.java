package com.fast_campus_12.not_found.shop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    private Long userId;          // USER_ID (FK)
    private String email;         // EMAIL
    private String name;          // NAME
    private String phoneNumber;   // PHONE_NUMBER
    private LocalDate birthDate;  // BIRTH_DATE
    private String gender;        // GENDER (M, F)
    private Integer jobCode;      // JOB_CODE (필수 필드)
}
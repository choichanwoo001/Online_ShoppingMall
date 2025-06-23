package com.fast_campus_12.not_found.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {
    private String userId;            // FK (USERS.id 참조)
    private String email;           // 이메일
    private String name;            // 이름
    private String phoneNumber;     // 전화번호
    private LocalDate birthDate;    // 생년월일
    private String gender;          // 성별
}

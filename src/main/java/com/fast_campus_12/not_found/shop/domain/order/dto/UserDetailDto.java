package com.fast_campus_12.not_found.shop.domain.order.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDetailDto {
    private String userId;        // 로그인ID (PK)
    private String email;          // 이메일 (유니크)
    private String name;           // 이름
    private String phoneNumber;    // 전화번호
    private LocalDate birthDate;   // 생년월일
    private String gender;         // 성별 (M, F)
    private Integer jobCode;       // 직업 코드 (인덱스)
}

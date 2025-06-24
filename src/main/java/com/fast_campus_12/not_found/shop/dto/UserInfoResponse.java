package com.fast_campus_12.not_found.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    // USERS 테이블 기본 정보
    private String userId;          // LOGIN_ID (화면 표시용)
    private Boolean isActivate;     // 활성화 상태
    private Boolean isDeleted;      // 삭제 상태
    private String role;            // 역할
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime updatedAt; // 수정일시

    // USER_DETAIL 테이블 상세 정보
    private String userName;        // 이름
    private String email;           // 이메일
    private String mobilePhone;     // 휴대폰번호
    private String gender;          // 성별
    private LocalDate birthDate;    // 생년월일
    private Integer jobCode;        // 직업코드

    // DEFAULT_USER_ADDRESS 테이블 주소 정보
    private String roadAddress1;    // 도로명주소1
    private String roadAddress2;    // 도로명주소2
    private String jibunAddress;    // 지번주소
    private String detailAddress;   // 상세주소
    private String englishAddress;  // 영문주소
    private Integer zipCode;        // 우편번호
    private String addressName;     // 주소명

    // 화면 표시용 필드 (하위 호환성)
    private String postcode;        // 우편번호 (String)
    private String address;         // 조합된 주소
}
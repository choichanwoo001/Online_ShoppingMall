package com.fast_campus_12.not_found.shop.domain.user.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    // 식별자 (LOGIN_ID)
    private String userId;          // LOGIN_ID (사용자가 입력하는 아이디)

    // 수정 가능한 기본 정보
    private String password;        // 새 비밀번호 (선택사항)
    private String userName;        // 이름
    private String email;           // 이메일
    private String mobilePhone;     // 휴대폰번호
    private String gender;          // 성별
    private String birthDate;       // 생년월일
    private Integer jobCode = 1;  // 기본값 1로 설정

    // 주소 정보
    private String roadAddress1;    // 도로명주소1
    private String roadAddress2;    // 도로명주소2
    private String jibunAddress;    // 지번주소
    private String detailAddress;   // 상세주소
    private String englishAddress;  // 영문주소
    private String zipCode;         // 우편번호 (String)
    private String addressName;     // 주소명
}
package com.fast_campus_12.not_found.shop.dto;

import lombok.Data;

@Data
public class SignupRequest {
    // 기본 회원 정보
    private String userId;
    private String password;
    private String userName;
    private String email;
    private String mobilePhone;

    // 주소 관련 필드들 (ERD에 맞춰)
    private String roadAddress1;    // 도로명 주소 1 - VARCHAR
    private String roadAddress2;    // 도로명 주소 2 - VARCHAR(255)
    private String jibunAddress;    // 지번 주소 - VARCHAR(255)
    private String detailAddress;   // 상세 주소 - VARCHAR(255)
    private String englishAddress;  // 영문 주소 - VARCHAR(255)
    private Integer zipCode;        // 우편번호 - INT
    private String addressName;     // 주소명 - VARCHAR

    // 화면 표시용 필드들 (하위 호환성)
    private String address;         // 조합된 주소 (화면 표시용)
    private String postcode;        // 우편번호 문자열 (화면 표시용)
}
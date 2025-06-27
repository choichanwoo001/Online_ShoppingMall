package com.fast_campus_12.not_found.shop.dto;

import lombok.Data;

@Data
public class SignupRequest {
    // 사용자 기본 정보
    private String userId;          // LOGIN_ID (사용자가 입력하는 아이디)
    private String password;        // 비밀번호
    private String userName;        // 이름
    private String email;           // 이메일
    private String mobilePhone;     // 휴대폰번호
    private String gender;          // 성별 (M, F)
    private String birthDate;       // 생년월일
    private Integer jobCode;        // 직업코드 (기본값: 1)

    // 주소 정보
    private String address;         // 기본 주소 (하위 호환성)
    private String roadAddress1;    // 도로명 주소1
    private String roadAddress2;    // 도로명 주소2
    private String jibunAddress;    // 지번 주소
    private String detailAddress;   // 상세 주소
    private String englishAddress;  // 영문 주소
    private String zipCode;         // 우편번호 (String으로 받아서 Integer로 변환)
    private String addressName;     // 주소명

    // 기본값 설정
    public Integer getJobCode() {
        return jobCode != null ? jobCode : 1; // 기본값 1
    }
}
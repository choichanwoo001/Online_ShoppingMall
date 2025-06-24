
// UserUpdateRequest.java
package com.fast_campus_12.not_found.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String userId;
    private String password;        // null이면 비밀번호 변경하지 않음
    private String userName;
    private String email;
    private String mobilePhone;
    private String gender;          // M, F
    private String birthDate;       // yyyy-MM-dd 형식

    // 주소 정보
    private String postcode;        // 화면 표시용
    private String address;         // 화면 표시용
    private String detailAddress;
    private String roadAddress1;
    private String roadAddress2;
    private String jibunAddress;
    private String englishAddress;
    private String zipCode;
    private String addressName;
}
// UserInfoResponse.java
package com.fast_campus_12.not_found.shop.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String userId;
    private String userName;
    private String email;
    private String mobilePhone;
    private String gender;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 주소 정보
    private String postcode;
    private String address;
    private String detailAddress;
    private String roadAddress1;
    private String roadAddress2;
    private String jibunAddress;
    private String englishAddress;
    private Integer zipCode;
    private String addressName;

    // 계정 상태
    private Boolean isActivate;
    private Boolean isDeleted;
    private String role;
}
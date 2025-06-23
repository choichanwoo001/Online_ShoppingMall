package com.fast_campus_12.not_found.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// UserAddress.java (ERD에 맞춘 Entity)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {
    private String userId;          // FK (USERS.user_id 참조) - BIGINT
    private String roadAddress1;    // 도로명 주소 1 - VARCHAR
    private String roadAddress2;    // 도로명 주소 2 - VARCHAR(255)
    private String jibunAddress;    // 지번 주소 - VARCHAR(255)
    private String detailAddress;   // 상세 주소 - VARCHAR(255)
    private String englishAddress;  // 영문 주소 - VARCHAR(255)
    private Integer zipCode;        // 우편번호 - INT
    private String addressName;     // 주소명 - VARCHAR
}

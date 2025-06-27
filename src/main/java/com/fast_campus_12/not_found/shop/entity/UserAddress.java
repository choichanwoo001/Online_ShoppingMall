package com.fast_campus_12.not_found.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
    private Long userId;          // USER_ID (FK)
    private String roadAddress1;  // ROAD_ADDRESS_1
    private String roadAddress2;  // ROAD_ADDRESS_2
    private String jibunAddress;  // JIBUN_ADDRESS
    private String detailAddress; // DETAIL_ADDRESS
    private String englishAddress; // ENGLISH_ADDRESS
    private Integer zipCode;      // ZIP_CODE
    private String addressName;   // ADDRESS_NAME
}
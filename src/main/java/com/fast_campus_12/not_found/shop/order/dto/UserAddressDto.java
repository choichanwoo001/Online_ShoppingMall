package com.fast_campus_12.not_found.shop.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserAddressDto {
    private String userId;             // 로그인ID(PK)
    private String roadAddress1;        // 도로명주소1
    private String roadAddress2;        // 도로명주소2
    private String jibunAddress;        // 지번주소
    private String detailAddress;       // 상세주소
    private String englishAddress;      // 영문주소
    private String zipCode;             // 우편번호
    private String addressName;     // 주소별칭
}

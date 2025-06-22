package com.fast_campus_12.not_found.shop.entity;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userId;     // user_id (실제 로그인 아이디)
    private String password;
    private String userName;   // name
    private String email;
    private String address;
    private String detailAddress;
    private String mobilePhone; // phone_number
    private Boolean isActive;   // is_activate
    private Boolean isDeleted;  // is_deleted
    private String role; // 이건 회원이냐 관리자냐 구분 필드긴함
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    // 기본 생성자
}
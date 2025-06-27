package com.fast_campus_12.not_found.shop.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class OrderUserDetailServiceDto {
   private String loginID;
   private String emailId;
   private String emailDomain;
   private String midPhoneNum;
   private String lastPhoneNum;
   private String name;
}

package com.fast_campus_12.not_found.shop.domain.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@RequiredArgsConstructor
public class OrderRequestDto {
    private String orderId;
    private String name;
    private String emailId;
    private String emailDomain;
    private String firstPhoneNum;
    private String midPhoneNum;
    private String lastPhoneNum;
    private String receiverName;
    private String receiverFirstPhone;
    private String receiverMidPhone;
    private String receiverLastPhone;
    private String receiverZipcode;
    private String receiverAddress1;
    private String receiverAddress2;
    private boolean defaultAddress;
    private Long couponId;
    private int useMileage;
    private String paymentMethod;
    private boolean agreeAll;
    private int shippingFee;
    private int finalAmount;

    private List<ProductOrderInfoDto> items;
}

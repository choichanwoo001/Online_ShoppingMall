package com.fast_campus_12.not_found.shop.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderData {
    // 주문자 정보
    private String name;
    private String email;
    private String phone;
    private String zipcode;
    private String address1;
    private String address2;

    // 수령자 정보
    private String receiverName;
    private String receiverPhone;
    private String receiverZipcode;
    private String receiverAddress1;
    private String receiverAddress2;
    private boolean defaultAddress;

    // 주문 상품 정보
    private List<String> itemQuantities;
    private List<String> itemPrices;
    private List<String> itemProductNames;

    // 할인 정보
    private String couponId;
    private String useMileage;
    private String appliedCouponId;
    private String finalUsedMileage;
    private String finalAmount;
    private String shippingFee;

    // 결제 정보
    private String paymentMethod;
    private boolean agreeAll;

}
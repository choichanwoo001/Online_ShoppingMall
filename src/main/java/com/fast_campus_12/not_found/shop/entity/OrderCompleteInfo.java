package com.fast_campus_12.not_found.shop.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCompleteInfo {
    private String orderId;
    private String formattedOrderDate;
    private String orderStatus;
    private String paymentMethodName;

    // 주문자 정보
    private String ordererName;
    private String ordererEmail;
    private String ordererPhone;

    // 수령자 정보
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    // 주문 상품
    private List<OrderItem> orderItems;

    // 결제 정보
    private int totalItemPrice;
    private int shippingFee;
    private int couponDiscount;
    private String couponName;
    private int mileageUsed;
    private int finalAmount;
    private int earnedMileage;


}
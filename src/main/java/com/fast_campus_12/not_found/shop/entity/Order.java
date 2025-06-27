package com.fast_campus_12.not_found.shop.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order {

    // 주문자 정보
    private String name;
    private String emailId;
    private String emailDomain;
    private String firstPhoneNum;
    private String midPhoneNum;
    private String lastPhoneNum;

    // 주문자 주소
    private String zipcode;
    private String address1;
    private String address2;

    // 수령자 정보
    private String receiverName;
    private String receiverFirstPhone;
    private String receiverMidPhone;
    private String receiverLastPhone;

    // 수령자 주소
    private String receiverZipcode;
    private String receiverAddress1;
    private String receiverAddress2;
    private Boolean defaultAddress;

    // 주문 상품 정보
    private List<OrderItem> items;

    // 할인 정보
    private String appliedCouponId;
    private Integer finalUsedMileage;
    private Integer shippingFee;
    private Integer finalAmount;

    // 결제 정보
    private String paymentMethod;
    private Boolean agreeAll;

    // 주문 완료 후 추가 정보
    private String orderId;
    private String formattedOrderDate;
    private String orderStatus;
    private String paymentMethodName;
    private String couponName;
    private Integer couponDiscount;
    private Integer mileageUsed;
    private Integer totalItemPrice;
    private Integer earnedMileage;

    // 편의 메서드들
    public String getFullEmail() {
        if (emailId != null && emailDomain != null) {
            return emailId + "@" + emailDomain;
        }
        return null;
    }

    public String getOrdererFullPhone() {
        if (firstPhoneNum != null && midPhoneNum != null && lastPhoneNum != null) {
            return firstPhoneNum + "-" + midPhoneNum + "-" + lastPhoneNum;
        }
        return null;
    }

    public String getReceiverFullPhone() {
        if (receiverFirstPhone != null && receiverMidPhone != null && receiverLastPhone != null) {
            return receiverFirstPhone + "-" + receiverMidPhone + "-" + receiverLastPhone;
        }
        return null;
    }

    public String getOrdererFullAddress() {
        StringBuilder address = new StringBuilder();
        if (zipcode != null) address.append("(").append(zipcode).append(") ");
        if (address1 != null) address.append(address1);
        if (address2 != null) address.append(" ").append(address2);
        return address.toString().trim();
    }

    public String getReceiverFullAddress() {
        StringBuilder address = new StringBuilder();
        if (receiverZipcode != null) address.append("(").append(receiverZipcode).append(") ");
        if (receiverAddress1 != null) address.append(receiverAddress1);
        if (receiverAddress2 != null) address.append(" ").append(receiverAddress2);
        return address.toString().trim();
    }
}
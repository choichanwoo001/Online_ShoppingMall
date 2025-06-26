package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.mapper.OrderMapper;
import com.fast_campus_12.not_found.shop.order.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fast_campus_12.not_found.shop.entity.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;

    public OrderUserDetailServiceDto UserDetailSplit(String userId){

        UserDetailDto userDetail = orderMapper.finUserDetailByUserId(userId);

        // 유저 null검사
        if (userDetail == null) {
            throw new IllegalArgumentException("해당 유저의 정보가 존재하지 않습니다: " + userId);
        }

        // 이메일 분리
        String[] emailParts = userDetail.getEmail().split("@");
        String emailId = emailParts[0];
        String emailDomain = emailParts.length > 1 ? emailParts[1] : "";

        //  휴대전화 분리
        String[] phoneParts = userDetail.getPhoneNumber().split("-");
        String midPhoneNum = phoneParts.length > 1 ? phoneParts[1] : "";
        String lastPhoneNum = phoneParts.length > 2 ? phoneParts[2] : "";

        return OrderUserDetailServiceDto.builder()
                .emailId(emailId)
                .emailDomain(emailDomain)
                .midPhoneNum(midPhoneNum)
                .lastPhoneNum(lastPhoneNum)
                .userId(userId)
                .name(userDetail.getName())
                .build();
    }


    // 유저 상세 주소 조회
    public UserAddressDto getUserAddress(String userId) {
        return orderMapper.findUserAddressByUserId(userId);
    }

    // 유저 주문 상품 조회 장바구니 기준
    public List<ProductOrderInfoDto> getOrdersInfo(String userId) {
        return orderMapper.findCartItemsForOrderByUserId(userId);
    }

    // 유저 쿠폰 조회
    public List<CouponDto> getOrdersCouponInfo(String userId) {
        return orderMapper.findUserCouponsByUserId(userId);
    }

    // 유저 마일리지 조회
    public MileageDto getAvailableMileage(String userId) {
        return orderMapper.findAvailableMileageByUserId(userId);
    }

    //  배송비는 우선 하드코딩으로
    public int shippingFee(){
        return 2500;
    }

    public static String generateOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String datePart = LocalDateTime.now().format(formatter);

        int randomPart = new Random().nextInt(90000) + 10000; // 5자리 랜덤 숫자

        return "ORD-" + datePart + "-" + randomPart;
    } // 주문번호 생성 메서드


}

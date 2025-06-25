package com.fast_campus_12.not_found.shop.service;

import com.fast_campus_12.not_found.shop.mapper.OrderMapper;
import com.fast_campus_12.not_found.shop.order.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        OrderUserDetailServiceDto orderServiceDto = new OrderUserDetailServiceDto();


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


}

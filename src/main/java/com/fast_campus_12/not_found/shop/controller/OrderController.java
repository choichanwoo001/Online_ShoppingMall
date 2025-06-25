
package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.entity.OrderCompleteInfo;
import com.fast_campus_12.not_found.shop.entity.OrderItem;
import com.fast_campus_12.not_found.shop.order.dto.*;
import com.fast_campus_12.not_found.shop.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
    public class OrderController {

    @Autowired
    OrderMapper orderMapper;
    UserDetailDto userDetail;
    UserAddressDto userAddress;
    Long userId = 3L;
    private OrderCompleteInfo orderInfo;

    @GetMapping("/order/{pageName}")
    public String orderRenderPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("contentPath", "order/" + pageName);


        userDetail = orderMapper.finUserDetailByUserId(userId);
        model.addAttribute("userDetail", userDetail);

        // 2. 이메일 분리
        String[] emailParts = userDetail.getEmail().split("@");
        String emailId = emailParts[0];
        String emailDomain = emailParts.length > 1 ? emailParts[1] : "";

        // 3. 휴대전화 분리
        String[] phoneParts = userDetail.getPhoneNumber().split("-");
        String midPhoneNum = phoneParts.length > 1 ? phoneParts[1] : "";
        String lastPhoneNum = phoneParts.length > 2 ? phoneParts[2] : "";

        // 4. 주소 객체 (null 허용 가능하도록)
        userAddress = orderMapper.findUserAddressByUserId(userId);


        // 5. 주문 상품 리스트
        List<ProductOrderInfoDto> orderItems = orderMapper.findCartItemsForOrderByUserId(userId);

        // 6. 쿠폰
        List<CouponDto> couponList  = orderMapper.findUserCouponsByUserId(userId);

        // 7. 마일리지
        MileageDto availableMileage = orderMapper.findAvailableMileageByUserId(userId);

        // 8. 기타 값
        int shippingFee = 2500;

        // 7. Model에 담기
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("address", userAddress);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("couponList", couponList);
        model.addAttribute("availableMileage", availableMileage.getAvailableMileage());
        model.addAttribute("shippingFee", shippingFee);

        return "layout/base";
    }

    @PostMapping("/order/order")
    public String processOrder(
            // 주문자 정보
            @RequestParam("name") String name,
            @RequestParam("emailId") String emailId,
            @RequestParam("emailDomain") String emailDomain,
            @RequestParam("firstPhoneNum") String firstPhoneNum,
            @RequestParam("midPhoneNum") String midPhoneNum,
            @RequestParam("lastPhoneNum") String lastPhoneNum,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address1") String address1,
            @RequestParam("address2") String address2,

            // 수령자 정보
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverFirstPhone") String receiverFirstPhone,
            @RequestParam("receiverMidPhone") String receiverMidPhone,
            @RequestParam("receiverLastPhone") String receiverLastPhone,
            @RequestParam("receiverZipcode") String receiverZipcode,
            @RequestParam("receiverAddress1") String receiverAddress1,
            @RequestParam("receiverAddress2") String receiverAddress2,
            @RequestParam(value = "defaultAddress", required = false) String defaultAddress,

            // 주문 상품 정보
            @RequestParam("items[0].quantity") List<String> itemQuantities,
            @RequestParam("items[0].price") List<String> itemPrices,
            @RequestParam("items[0].productName") List<String> itemProductNames,

            // 할인 정보
            @RequestParam(value = "couponId", required = false) String couponId,
            @RequestParam("useMileage") String useMileage,
            @RequestParam("appliedCouponId") String appliedCouponId,
            @RequestParam("finalUsedMileage") String finalUsedMileage,
            @RequestParam("finalAmount") String finalAmount,
            @RequestParam("shippingFee") String shippingFee,

            // 결제 정보
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam(value = "agreeAll", required = false) String agreeAll,

            Model model) {


        try {
            // 이메일 조합
            String email = emailId + "@" + emailDomain;

            // 전화번호 조합
            String phone = firstPhoneNum + "-" + midPhoneNum + "-" + lastPhoneNum;
            String receiverPhone = receiverFirstPhone + "-" + receiverMidPhone + "-" + receiverLastPhone;

            // 주소 조합
            String fullAddress = address1 + " " + address2;
            String receiverFullAddress = receiverAddress1 + " " + receiverAddress2;

            // 주문번호 생성 (현재 시간 기반)
            String orderId = "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 주문 상품 리스트 생성
            List<OrderItem> orderItems = new ArrayList<>();
            for (int i = 0; i < itemProductNames.size(); i++) {
                OrderItem item = new OrderItem();
                item.setProductName(itemProductNames.get(i));
                item.setQuantity(Integer.parseInt(itemQuantities.get(i)));
                item.setTotalPrice(Integer.parseInt(itemPrices.get(i)));
                orderItems.add(item);
            }

            // 상품 총액 계산
            int totalItemPrice = orderItems.stream()
                    .mapToInt(OrderItem::getTotalPrice)
                    .sum();

            // 쿠폰 할인액 계산 (예시)
            int couponDiscount = 0;
            String couponName = "";
            if (appliedCouponId != null && !appliedCouponId.isEmpty() && !appliedCouponId.equals("0")) {
                // 실제로는 쿠폰 정보를 DB에서 조회해야 함
                couponDiscount = 5000; // 예시 할인액
                couponName = "신규회원 할인쿠폰"; // 예시 쿠폰명
            }

            // 적립금 사용액
            int mileageUsed = Integer.parseInt(finalUsedMileage);

            // 배송비
            int deliveryFee = Integer.parseInt(shippingFee);

            // 최종 결제 금액
            int finalPaymentAmount = Integer.parseInt(finalAmount);

            // 적립 예정 마일리지 (결제금액의 1%)
            int earnedMileage = (int) (finalPaymentAmount * 0.01);

            // 결제 방법명 변환
            String paymentMethodName = getPaymentMethodName(paymentMethod);

            // OrderInfo 객체 생성
            orderInfo.setOrderId(orderId);
            orderInfo.setFormattedOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            orderInfo.setOrderStatus("주문완료");
            orderInfo.setPaymentMethodName(paymentMethodName);

            // 주문자 정보
            orderInfo.setOrdererName(name);
            orderInfo.setOrdererEmail(email);
            orderInfo.setOrdererPhone(phone);

            // 수령자 정보
            orderInfo.setReceiverName(receiverName);
            orderInfo.setReceiverPhone(receiverPhone);
            orderInfo.setReceiverAddress(receiverFullAddress);

            // 주문 상품 정보
            orderInfo.setOrderItems(orderItems);

            // 결제 정보
            orderInfo.setTotalItemPrice(totalItemPrice);
            orderInfo.setShippingFee(deliveryFee);
            orderInfo.setCouponDiscount(couponDiscount);
            orderInfo.setCouponName(couponName);
            orderInfo.setMileageUsed(mileageUsed);
            orderInfo.setFinalAmount(finalPaymentAmount);
            orderInfo.setEarnedMileage(earnedMileage);

            // 모델에 주문 정보 추가
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("message", "주문이 성공적으로 완료되었습니다.");

            // 여기서 실제 주문 처리 로직 (DB 저장 등)을 수행
            // orderService.saveOrder(orderInfo);

        } catch (Exception e) {
            // 에러 처리
            model.addAttribute("error", "주문 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "layout/base"; // 에러가 발생해도 같은 페이지로
        }

        // 주문 완료 페이지 직접 반환 (redirect 하지 않음)
        return "layout/base";
    }

    // 주문 완료 페이지 GET 컨트롤러 (URL 직접 접근용)
    @GetMapping("order/ordercomplete")
    public String orderComplete(Model model) {

        // URL 직접 접근 시에는 기본 메시지만 표시
        model.addAttribute("message", "주문 완료 페이지입니다.");
        return "layout/base";


    }
    // 결제 방법 코드를 이름으로 변환하는 헬퍼 메서드
    private String getPaymentMethodName(String paymentMethod) {
        switch (paymentMethod) {
            case "card":
                return "카드결제";
            case "transfer":
                return "계좌이체";
            case "phone":
                return "휴대폰결제";
            case "virtual":
                return "가상계좌";
            default:
                return "기타";
        }
    }
}



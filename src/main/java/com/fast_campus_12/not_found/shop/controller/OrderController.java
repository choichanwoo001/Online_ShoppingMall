
package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.order.dto.SnapShotDto;
import com.fast_campus_12.not_found.shop.order.dto.UserAddressDto;
import com.fast_campus_12.not_found.shop.order.dto.UserDetailDto;
import com.fast_campus_12.not_found.shop.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
    public class OrderController {

    @Autowired
    OrderMapper orderMapper;
    UserDetailDto userDetail;
    UserAddressDto userAddress;
    String userId = "3";

    @GetMapping("/order/{pageName}")
    public String orderRenderPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("contentPath", "order/" + pageName);


        userDetail = orderMapper.findByUserId(userId);
//            userDetail.setName("홍길동");
        model.addAttribute("userDetail", userDetail);

        // 2. 이메일 분리
//            userDetail.setEmail("test@test");
        String[] emailParts = userDetail.getEmail().split("@");
        String emailId = emailParts[0];
        String emailDomain = emailParts.length > 1 ? emailParts[1] : "";

        // 3. 휴대전화 분리
//            userDetail.setPhoneNumber("111-2222-3333");
        String[] phoneParts = userDetail.getPhoneNumber().split("-");
        String midPhoneNum = phoneParts.length > 1 ? phoneParts[1] : "";
        String lastPhoneNum = phoneParts.length > 2 ? phoneParts[2] : "";

        // 4. 주소 객체 (null 허용 가능하도록)
        userAddress = orderMapper.findAddressByUserId(userId);


        // 5. 주문 상품 리스트
        List<SnapShotDto> snapShotDto = new ArrayList<>();
        snapShotDto.add(new SnapShotDto(10000, "상품1"));
        snapShotDto.add(new SnapShotDto(20000, "상품2"));
        model.addAttribute("snapShotDto", snapShotDto);
        // 6. 기타 값
        int shippingFee = 2500;
        int availableMileage = 1200;
//          마일리지 셀렉트 기준은 user_id기준

        // 7. Model에 담기
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("address", userAddress);
        model.addAttribute("orderItems", snapShotDto);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("availableMileage", availableMileage);

        return "layout/base";
    }

    @PostMapping("/order/order")
    public String handleOrderSubmit(
            @RequestParam("name") String name,
            @RequestParam("fullEmail") String fullEmail,
            @RequestParam("fullPhoneNumber") String fullPhoneNumber,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address1") String address1,
            @RequestParam("address2") String address2,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam(value = "useMileage", required = false, defaultValue = "0") int useMileage,
            RedirectAttributes redirectAttributes
    ) {
        // 간단히 받은 정보 로그나 처리 로직
        System.out.println("주문자: " + name);
        System.out.println("이메일: " + fullEmail);
        System.out.println("전화번호: " + fullPhoneNumber);
        System.out.println("주소: " + zipcode + ", " + address1 + ", " + address2);
        System.out.println("결제수단: " + paymentMethod);
        System.out.println("사용 마일리지: " + useMileage);

        // 리다이렉트 시 FlashAttributes로 데이터 전달
        redirectAttributes.addFlashAttribute("orderName", name);
        redirectAttributes.addFlashAttribute("orderEmail", fullEmail);
        redirectAttributes.addFlashAttribute("orderPhone", fullPhoneNumber);
        redirectAttributes.addFlashAttribute("zipCode", zipcode);
        redirectAttributes.addFlashAttribute("address1", address1);
        redirectAttributes.addFlashAttribute("address2", address2);
        redirectAttributes.addFlashAttribute("paymentMethod", paymentMethod);
        redirectAttributes.addFlashAttribute("useMileage", useMileage);


        return "redirect:/order/ordercomplete";
    }

}



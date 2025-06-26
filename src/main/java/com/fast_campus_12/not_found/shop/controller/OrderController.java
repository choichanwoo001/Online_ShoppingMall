
package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.entity.Order;
import com.fast_campus_12.not_found.shop.order.dto.*;
import com.fast_campus_12.not_found.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping("/order/{pageName}")
    public String orderRenderPage(@PathVariable("pageName") String pageName, HttpSession session, Model model) {
        model.addAttribute("contentPath", "order/" + pageName);

        session.setAttribute("userId", "3");
        String sessionId = (String) session.getAttribute("userId");

//        if (sessionId != null) {
//            model.addAttribute("error", "로그인이 필요합니다.");
//            return "redirect:/login";
//        }

        OrderUserDetailServiceDto orderDetailServiceDto =  orderService.UserDetailSplit(sessionId);
        // 유저 상세 주소 조회
        UserAddressDto userAddressDto = orderService.getUserAddress(sessionId);
        // 유저 주문 상품 조회 장바구니 기준
        List<ProductOrderInfoDto> productOrderInfoDto = orderService.getOrdersInfo(sessionId);
        // 유저 쿠폰 조회
        List<CouponDto> couponDto = orderService.getOrdersCouponInfo(sessionId);
        // 유저 마일리지 조회
        MileageDto mileageDto = orderService.getAvailableMileage(sessionId);



        // 7. Model에 담기
        model.addAttribute("userDetail", orderDetailServiceDto);
        model.addAttribute("address", userAddressDto);
        model.addAttribute("orderItems", productOrderInfoDto);
        model.addAttribute("couponList", couponDto);
        model.addAttribute("availableMileage", mileageDto.getAvailableMileage());
        model.addAttribute("shippingFee", orderService.shippingFee());

        return "layout/base";
    }

    @PostMapping("/order/order")
    public String processOrder(@ModelAttribute  OrderUserDetailServiceDto orderDetailServiceDto,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        String orderId = OrderService.generateOrderId();

        redirectAttributes.addFlashAttribute("orderId", orderId);
        redirectAttributes.addFlashAttribute("orderDetailServiceDto", orderDetailServiceDto);
        model.addAttribute("contentPath", "order/ordercomplete");



        return "layout/base";
    }

    @GetMapping("/order1/{ordercomplete}")
    public String orderComplete(@PathVariable("ordercomplete") String ordercompleteJson,
                                @ModelAttribute("orderId") String orderId,
                                Model model, Order orderInfo) {

        if (orderId == null || orderId.isEmpty()) {
            model.addAttribute("message", "주문 중 오류가 발생했습니다.");
            return "redirect:/layout/base";
        }
        model.addAttribute("orderInfo", orderId);

        String ordererPhone = orderInfo.getOrdererFullPhone();
        String ordererEmail = orderInfo.getFullEmail();

        model.addAttribute("orderInfo", ordererPhone);
        model.addAttribute("orderInfo", ordererEmail);

        try {
            model.addAttribute("message", "주문 완료 페이지입니다.");
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("contentPath", "order/ordercomplete");
        } catch (Exception e) {
            model.addAttribute("error", "데이터를 불러오는 중 오류 발생: " + e.getMessage());
            model.addAttribute("contentPath", "order/ordercomplete");
        }

        return "layout/base";
    }

}



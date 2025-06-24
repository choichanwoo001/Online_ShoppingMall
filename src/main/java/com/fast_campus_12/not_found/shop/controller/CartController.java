package com.fast_campus_12.not_found.shop.controller;

import com.fast_campus_12.not_found.shop.mapper.CartItemMapper;
import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.Cart;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import com.fast_campus_12.not_found.shop.product.service.CartService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/order/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemMapper cartItemMapper;

    //cart url 매핑 임시 + 팝업
    @GetMapping
    public String showPopup(Model model) {
        model.addAttribute("contentPath", "order/cart");

        // 예시: 동적으로 팝업 데이터 생성 또는 DB에서 조회
        String dynamicTitle = "새로운 소식!";
        String dynamicMessage = "<p>오늘의 <b>특별 할인</b> 이벤트를 놓치지 마세요!</p>";
        boolean showPopupCloseButton = true;

        // Model에 팝업 데이터를 담아서 Thymeleaf로 전달
        // (이 방식은 팝업 내용을 페이지 컨트롤러에서 직접 제어할 때 사용)
        model.addAttribute("popupTitle", dynamicTitle);
        model.addAttribute("popupMessage", dynamicMessage);
        model.addAttribute("showCloseButton", showPopupCloseButton);


        return "layout/base";
    }


    /**
     * 장바구니 조회  테스트
     */
    @GetMapping("/test")
    public String CartTest(Model model) {
        model.addAttribute("contentPath", "order/cart");
// CartItem 1 생성
        // 더미 CartItem 1
        CartItemViewDto item1 = new CartItemViewDto();
        item1.setCartItemId("CI001");
        item1.setProductVariantId(1001L);
        item1.setProductName("Farben 바스락 싱글자켓");
        item1.setColor("블랙");
        item1.setSize("FREE");
        item1.setImageUrl("https://via.placeholder.com/100x120");
        item1.setQuantity(2);
        item1.setPrice(93000);

        // 더미 CartItem 2
        CartItemViewDto item2 = new CartItemViewDto();
        item2.setCartItemId("CI002");
        item2.setProductVariantId(1002L);
        item2.setProductName("마일드 니트 가디건");
        item2.setColor("아이보리");
        item2.setSize("M");
        item2.setImageUrl("https://via.placeholder.com/100x120");
        item2.setQuantity(1);
        item2.setPrice(67000);

        List<CartItemViewDto> cartItems = new ArrayList<>();
        cartItems.add(item1);
        cartItems.add(item2);

        model.addAttribute("cartItems", cartItems);
        return "layout/base";
    }

}




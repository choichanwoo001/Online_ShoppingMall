//package com.fast_campus_12.not_found.shop.product.conrtoller;
//
//
//import com.fast_campus_12.not_found.shop.product.service.*;
//import com.fast_campus_12.not_found.shop.product.dto.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/product")
//class ProductViewController {
//    private final ProductService productService;
//
//    @GetMapping("/{id}")
//    public String showProduct(@PathVariable Long id, Model model) {
//        ProductDetailDto dto = productService.getProductDetail(id);
//        if (dto == null) return "error/404";
//        model.addAttribute("product", dto);
//        return "page/product/detail";
//    }
//}
//
//// 실제 기능 메서드(GET/POST/PUT/DELETE)는 이후 필요에 따라 구체적으로 구현

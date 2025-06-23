package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ColorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/color")
class ColorsViewController {
    private final ColorsService colorsService;

    @GetMapping("/list")
    public String colorList(Model model) {
        return "page/product/colors";
    }
}

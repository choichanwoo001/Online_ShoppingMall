package com.fast_campus_12.not_found.shop.product.conrtoller;

import com.fast_campus_12.not_found.shop.product.service.ColorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorsController {
    private final ColorsService colorsService;

    @GetMapping
    public String getAll() {
        return "get all colors";
    }
}

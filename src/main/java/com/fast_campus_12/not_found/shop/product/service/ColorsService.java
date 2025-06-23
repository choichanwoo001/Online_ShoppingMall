package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ColorDto;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ColorsService {
    public List<ColorDto> getAll() {
        return List.of(
                ColorDto.builder()
                        .colorId(BigInteger.ONE)
                        .name("Navy")
                        .hexCode("#001F3F")
                        .isActive(true)
                        .build()
        );
    }
}

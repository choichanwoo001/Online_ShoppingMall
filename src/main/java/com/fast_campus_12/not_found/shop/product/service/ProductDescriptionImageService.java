package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductDescriptionImageDto;
import com.fast_campus_12.not_found.shop.product.model.ImageFormat;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ProductDescriptionImageService {
    public List<ProductDescriptionImageDto> getAll() {
        return List.of(
                ProductDescriptionImageDto.builder()
                        .imageId(BigInteger.ONE)
                        .showDevice("ALL")
                        .url("/desc/image1.jpg")
                        .format(ImageFormat.JPG)
                        .renderPriority(1)
                        .build()
        );
    }
}

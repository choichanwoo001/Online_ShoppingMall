package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.mapper.ProductDynamicQueryMapper;
import com.fast_campus_12.not_found.shop.product.dto.ProductSpecialSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductPageDto;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryRequestDto;
import com.fast_campus_12.not_found.shop.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("dev")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DevProductServiceImpl implements ProductService {

    private final ProductDynamicQueryMapper productDynamicQueryMapper;


    @Override
    public ProductPageDto getSummaryByCategory(ProductSummaryRequestDto productSummaryRequestDto) {
        List<Product> productList = productDynamicQueryMapper
                .findProductListByCategory(productSummaryRequestDto);

        int totalCount = productDynamicQueryMapper
                .countProductListByCategory(productSummaryRequestDto);

        return ProductPageDto.builder()
                .totalCount(totalCount)
                .items(toSummaryDtoList(productList))
                .build();
    }

    @Override
    public ProductPageDto getSummaryBySpecialCategory(ProductSpecialSummaryRequestDto ProductSpecialSummaryRequestDto) {
        List<Product> productList = productDynamicQueryMapper
                .findProductListBySpecialTag(ProductSpecialSummaryRequestDto);

        int totalCount = productDynamicQueryMapper
                .countProductListBySpecialTag(ProductSpecialSummaryRequestDto);
        return ProductPageDto.builder()
                .totalCount(totalCount)
                .items(toSummaryDtoList(productList))
                .build();
    }


    public List<ProductSummaryDto> toSummaryDtoList(List<Product> products) {
        return products.stream()
                .map(p -> ProductSummaryDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .thumbnail(p.getThumbnail())
                        .price(p.getPrice())
//                        .reviewCount(p.getReviewCount())
                        .comment(p.getComment())
                        .build())
                .collect(Collectors.toList());
    }
}

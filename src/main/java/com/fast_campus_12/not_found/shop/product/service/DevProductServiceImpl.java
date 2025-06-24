package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.mapper.ProductDynamicQueryMapper;
import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
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
    public List<ProductSummaryDto> getSummaryByCategory(String lv2CategoryName, String lv3CategoryName, int offset, int limit) {
        List<Product> productList = productDynamicQueryMapper.findProductListByCategory(lv2CategoryName, lv3CategoryName, offset, limit);
        return toSummaryDtoList(productList);
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

    private List<ProductSummaryDto> getDummySummaryByCategory(String category) {
        return List.of(
                ProductSummaryDto.builder()
                        .id(1L)
                        .title("Product 1")
                        .price(3000)
                        .thumbnail("https://mblogthumb-phinf.pstatic.net/MjAxOTExMTRfMTcg/MDAxNTczNzEzNDIwMzkx.NtThUWxkOC4HvPQeHiEnhifFhrP2UOFgvEf3iOg21M8g.PaBwdhsT-CI9mddL5zTFTGEWfNm2Dsql5WNl6MjbiP8g.JPEG.silverwingkj/BIMO_비모.jpg?type=w800")
                        .comment("안녕하세요")
                        .reviewCount(3)
                        .build()
                ,
                ProductSummaryDto.builder()
                        .id(14L)
                        .title("Product 1")
                        .price(3000)
                        .thumbnail("https://mblogthumb-phinf.pstatic.net/MjAxOTExMTRfMTcg/MDAxNTczNzEzNDIwMzkx.NtThUWxkOC4HvPQeHiEnhifFhrP2UOFgvEf3iOg21M8g.PaBwdhsT-CI9mddL5zTFTGEWfNm2Dsql5WNl6MjbiP8g.JPEG.silverwingkj/BIMO_비모.jpg?type=w800")
                        .comment("안녕하세요")
                        .reviewCount(3)
                        .build()
                ,
                ProductSummaryDto.builder()
                        .id(13L)
                        .title("Product 1")
                        .price(3000)
                        .thumbnail("https://mblogthumb-phinf.pstatic.net/MjAxOTExMTRfMTcg/MDAxNTczNzEzNDIwMzkx.NtThUWxkOC4HvPQeHiEnhifFhrP2UOFgvEf3iOg21M8g.PaBwdhsT-CI9mddL5zTFTGEWfNm2Dsql5WNl6MjbiP8g.JPEG.silverwingkj/BIMO_비모.jpg?type=w800")
                        .comment("안녕하세요")
                        .reviewCount(3)
                        .build()
                ,
                ProductSummaryDto.builder()
                        .id(12L)
                        .title("Product 1")
                        .price(3000)
                        .thumbnail("https://mblogthumb-phinf.pstatic.net/MjAxOTExMTRfMTcg/MDAxNTczNzEzNDIwMzkx.NtThUWxkOC4HvPQeHiEnhifFhrP2UOFgvEf3iOg21M8g.PaBwdhsT-CI9mddL5zTFTGEWfNm2Dsql5WNl6MjbiP8g.JPEG.silverwingkj/BIMO_비모.jpg?type=w800")
                        .comment("안녕하세요")
                        .reviewCount(2)
                        .build()
        );
    }
}

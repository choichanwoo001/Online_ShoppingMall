package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.product.dto.ProductSummaryDto;
import com.fast_campus_12.not_found.shop.product.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
public class DevProductServiceImpl implements ProductService {
    @Override
    public List<ProductSummaryDto> getSummaryByCategory(String category) {
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

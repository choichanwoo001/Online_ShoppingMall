package com.fast_campus_12.not_found.shop.product.service;

import com.fast_campus_12.not_found.shop.mapper.ProductDynamicQueryMapper;
import com.fast_campus_12.not_found.shop.product.dto.*;
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

import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
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

    @Override
    public ProductDetailDto getProductDetailDto(BigInteger productId) {
        return getDummyProductDetailDto(productId);
    }

    public ProductDetailDto getDummyProductDetailDto(BigInteger productId) {
        ProductVariantDto variantDto = getDummyProductVariantDto(productId);

        List<SizeDto> sizeOptions = List.of(
                SizeDto.builder().sizeId(BigInteger.valueOf(1)).name("S").build(),
                SizeDto.builder().sizeId(BigInteger.valueOf(2)).name("M").build(),
                SizeDto.builder().sizeId(BigInteger.valueOf(3)).name("L").build()
        );

        List<ColorDto> colorOptions = List.of(
                ColorDto.builder().colorId(1).name("빨강").build(),
                ColorDto.builder().colorId(2).name("파랑").build(),
                ColorDto.builder().colorId(3).name("초록").build()
        );
//        List<ReviewDto> reviews = List.of(
//                ReviewDto.builder().username("jake23").createdDate("2024-05-10\"").content("옷이 정말 시원하고 핏도 좋아요! 추천합니다.").build(),
//                ReviewDto.builder().username("minseo87").createdDate("2024-06-01\"").content("배송도 빠르고 사이즈도 잘 맞아요").build()
//        );
        List<ReviewDto> reviews = List.of(
                ReviewDto.builder()
                        .username("jake23")
                        .createdDate("2024-05-10")
                        .content("좋네요 도구매하고싶어요ㅎㅎ")
                        .rating(5)
                        .images(List.of(
                                "https://picsum.photos/id/1011/200/200",
                                "https://picsum.photos/id/1012/200/200"
                        ))
                        .build(),

                ReviewDto.builder()
                        .username("minseo87")
                        .createdDate("2024-06-01")
                        .content("잘 맞고 예뻐요")
                        .rating(4)
                        .images(List.of(
                                "https://picsum.photos/id/1013/200/200"
                        ))
                        .build()
        );


        List<String> image = List.of(
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200"
        );
        List<String> detailImages = List.of(
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200",
                "https://picsum.photos/300/200"
        );




        return ProductDetailDto.builder().
                        productId(productId).
                        title("더미 셔츠").
                        sizes(sizeOptions).
                        colors(colorOptions).
                        thumbnail("https://picsum.photos/300/200").
                        detailImages(detailImages).
                        reviews(reviews).
                        productVariant(variantDto).
                        images(image).
                        summary("""
                                고피플에서 반팔중에 두번째로 많이 판매된 제품입니다,^^
                                
                                원래 이 제품이 1등이었는데 형제님들도 다들아시는
                                "SUPIMA 코튼 반팔티"가 이걸 이겨버렸네요,ㅎㅎ
                                
                                두 제품다 좋지만 오버핏 느낌은 위 수피마 코튼반팔이 좋고
                                스탠다드핏에 좀더 시원한 원단은 이 제품입니다,!
                                
                                흡한속건이 좋고 시원한 재질감의 반팔입니다 , 근육있는\s
                                분들은 소매두어번 접어 머슬느낌내서 입기에도 좋은 제품이구요
                                데일리용 여름반팔로 정말 강추드리는 제품입니다
                                고사장 L사이즈 편하게 잘 맞습니다:)
                                """).
                        model("""
                                - 181cm / 68kg L사이즈
                                
                                - 화이트,블랙,네이비,샌드,인디핑크,블루 컬러착용""").
                        size("""
                                M - 어깨 51cm / 가슴 57cm / 팔길이 22cm / 총길이 70cm
                                L - 어깨 53cm / 가슴 59cm / 팔길이 23cm / 총길이 71cm
                                XL - 어깨 55cm / 가슴 61cm / 팔길이 24cm / 총길이 72cm""").
                        code("P0000KCN").
                        comment("세트로 입으면 아주 이쁜 것 같아요^^").
                        savePoint(600). // 또는 price의 1%
                        quantity(1).
                        build();
    }
    public ProductVariantDto getProductVariantDto(BigInteger productId) {
        return getDummyProductVariantDto(productId);
    }

    public ProductVariantDto getDummyProductVariantDto(BigInteger productId) {
        return ProductVariantDto.builder().
                colorId(1).
                colorName("빨").
                sizeId(BigInteger.valueOf(1)).
                sizeName("XL").
                price(29000).
                build();
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

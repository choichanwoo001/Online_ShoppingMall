package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.domain.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.domain.product.model.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemMapper {

    // CartItemViewDto로 조회
    @Select("""
        SELECT 
            CI.CART_ITEM_ID AS cartItemId,
            CI.PRODUCT_VARIANT_ID AS productVariantId,
            P.PRODUCT_TITLE AS productName,
            PV.PRODUCT_VARIANT_COLOR_ID AS color,
            PV.PRODUCT_VARIANT_SIZE_ID AS size,
            P.PRODUCT_THUMBNAIL AS image_url,
            CONCAT('/product/', P.PRODUCT_ID) AS productUrl,
            CI.QUANTITY,
            POP.ORIGINAL_PRICE AS price,
            POP.SALE_PRICE AS discountPrice
        FROM CART_ITEM CI
        JOIN PRODUCT_VARIANT PV ON CI.PRODUCT_VARIANT_ID = PV.PRODUCT_VARIANT_ID
        JOIN PRODUCT_OPTION_PRICE POP ON CI.PRODUCT_VARIANT_ID = POP.PRODUCT_VARIANT_ID
        JOIN PRODUCT P ON PV.PRODUCT_ID = P.PRODUCT_ID
        WHERE CI.CART_ID = #{cartId}
    """)
    List<CartItemViewDto> findCartItemViewsByCartId(String cartId);

    @Select("SELECT * FROM CART_ITEM WHERE CART_ID = #{cartId}")
    @Results(id = "cartItemMap", value = {
            @Result(column = "CART_ITEM_ID", property = "id"),
            @Result(column = "CART_ID", property = "cartId"),
            @Result(column = "PRODUCT_VARIANT_ID", property = "productVariantId"),
            @Result(column = "QUANTITY", property = "quantity"),
            @Result(column = "UPDATED_AT", property = "updatedAt")
    })
    List<CartItem> findItemsByCartId(Long cartId);

    // 추가 - 대문자 테이블명으로 수정
    @Insert("INSERT INTO CART_ITEM (CART_ID, PRODUCT_VARIANT_ID, PRODUCT_ID, QUANTITY, UPDATED_AT) " +
            "SELECT #{cartId}, #{productVariantId}, PV.PRODUCT_ID, #{quantity}, NOW() " +
            "FROM PRODUCT_VARIANT PV " +
            "JOIN PRODUCT P ON PV.PRODUCT_ID = P.PRODUCT_ID " +
            "WHERE PV.PRODUCT_VARIANT_ID = #{productVariantId}")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "CART_ITEM_ID")
    void insertCartItem(CartItem cartItem);

    // 수량 업데이트 (이미 있는 상품인 경우)
    @Update("UPDATE CART_ITEM SET QUANTITY = #{quantity}, UPDATED_AT = NOW() " +
            "WHERE CART_ITEM_ID = #{id}")
    void updateQuantity(CartItem cartItem);

    // 중복 체크
    @Select("SELECT * FROM CART_ITEM WHERE CART_ID = #{cartId} AND PRODUCT_VARIANT_ID = #{productVariantId}")
    CartItem findByCartIdAndProductId(@Param("cartId") String cartId, @Param("productVariantId") Long productVariantId);

    @Delete("DELETE FROM CART_ITEM WHERE CART_ITEM_ID = #{cartItemId}")
    void deleteCartItem(String cartItemId);

    @Delete("DELETE FROM CART_ITEM WHERE CART_ID = #{cartId}")
    void deleteAllByCartId(String cartId);

    @Select("SELECT COUNT(*) FROM CART_ITEM WHERE CART_ID = #{cartId}")
    int countByCartId(String cartId);
}
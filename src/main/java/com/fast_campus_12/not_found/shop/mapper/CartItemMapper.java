package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemMapper {

    //CartItemViewDto로 조회
    @Select("""
        SELECT 
            ci.cart_item_id AS cartItemId,
            ci.product_variant_id AS productVariantId,
            p.product_title AS productName,
            pv.product_variant_color_id AS color,
            pv.product_variant_size_id AS size,
            p.product_thumbnail AS image_url,
            CONCAT('/product/', p.product_id) AS productUrl,
            ci.quantity,
            pop.original_price As price,
            pop.sale_price AS discountPrice
        FROM cart_item ci
        JOIN product_variant pv ON ci.product_variant_id = pv.product_variant_id
        JOIN product_option_price pop ON ci.product_variant_id = pop.product_variant_id
        JOIN product p ON pv.product_id = p.product_id
        WHERE ci.cart_id = #{cartId}
    """)
    List<CartItemViewDto> findCartItemViewsByCartId(String cartId);


    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId}")
    @Results(id = "cartItemMap", value = {
            @Result(column = "cart_item_id", property = "id"),
            @Result(column = "cart_id", property = "cartId"),
            @Result(column = "product_v_id", property = "productVariantId"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    List<CartItem> findItemsByCartId(Long cartId);

    // 추가
    // 더 안전한 버전 - 존재 여부 사전 확인
    @Insert("INSERT INTO cart_item (cart_id, product_variant_id, product_id, quantity, updated_at) " +
            "SELECT #{cartId}, #{productVariantId}, pv.PRODUCT_ID, #{quantity}, NOW() " +
            "FROM product_variant pv " +
            "INNER JOIN product p ON pv.PRODUCT_ID = p.PRODUCT_ID " +
            "WHERE pv.PRODUCT_VARIANT_ID = #{productVariantId} " +
            "AND pv.PRODUCT_ID IS NOT NULL " +
            "AND p.PRODUCT_ID IS NOT NULL")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "cart_item_id")
    void insertCartItem(CartItem cartItem);

    // 수량 업데이트 (이미 있는 상품인 경우)
    @Update("UPDATE cart_item SET quantity = #{quantity}, updated_at = NOW() " +
            "WHERE cart_item_id = #{id}")
    void updateQuantity(CartItem cartItem);

    // 중복 체크
    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId} AND product_variant_id = #{productVariantId}")
    CartItem findByCartIdAndProductId(@Param("cartId") String cartId, @Param("productVariantId") Long productVariantId);

    @Delete("DELETE FROM cart_item WHERE cart_item_id = #{cartItemId}")
    void deleteCartItem(String cartItemId);

    @Delete("DELETE FROM cart_item WHERE cart_id = #{cartId}")
    void deleteAllByCartId(String cartId);

    @Select("SELECT COUNT(*) FROM cart_item WHERE cart_id = #{cartId}")
    int countByCartId(String cartId);
}
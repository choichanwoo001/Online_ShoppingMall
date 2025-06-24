package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemMapper {

    @Select("""
        SELECT 
            ci.cart_item_id,
            ci.product_variant_id AS productVariantId,
            p.title AS productName,
            pv.color_id,
            pv.size,
            pv.image_url,
            ci.quantity,
            pv.price as price
            pv.price_modifier AS price
        FROM Cart_Item ci
        JOIN product_variant pv ON ci.product_variant_id = pv.product_variant_id
        JOIN product p ON pv.product_id = p.product_id
        WHERE ci.cart_id = #{cartId}
    """)
    List<CartItemViewDto> findCartItemViewsByCartId(String cartId);

    @Select("SELECT * FROM Cart_Item WHERE cart_id = #{cartId}")
    @Results(id = "cartItemMap", value = {
            @Result(column = "card_item_id", property = "id"),
            @Result(column = "cart_id", property = "cartId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "product_v_id", property = "productVId"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    List<CartItem> findItemsByCartId(Long cartId);

    // 추가
    @Insert("INSERT INTO Cart_Item (cart_id, product_id, product_v_id, quantity, updated_at) " +
            "VALUES (#{cartId}, #{productId}, #{productVId}, #{quantity}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "cart_item_id")
    void insertCartItem(CartItem cartItem);

    // 수량 업데이트 (이미 있는 상품인 경우)
    @Update("UPDATE Cart_Item SET quantity = #{quantity}, updated_at = NOW() " +
            "WHERE cart_id = #{cartId} AND product_id = #{productId}")
    void updateQuantity(CartItem cartItem);

    // 중복 체크
    @Select("SELECT * FROM Cart_Item WHERE cart_id = #{cartId} AND product_id = #{productId}")
    CartItem findByCartIdAndProductId(@Param("cartId") String cartId, @Param("productId") Long productId);

    @Delete("DELETE FROM Cart_Item WHERE cart_item_id = #{cartItemId}")
    void deleteCartItem(String cartItemId);

    @Delete("DELETE FROM Cart_Item WHERE cart_id = #{cartId}")
    void deleteAllByCartId(String cartId);

    @Select("SELECT COUNT(*) FROM Cart_Item WHERE cart_id = #{cartId}")
    int countByCartId(String cartId);
}
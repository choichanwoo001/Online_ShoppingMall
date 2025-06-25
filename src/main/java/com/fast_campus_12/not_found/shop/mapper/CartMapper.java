package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.Cart;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    // 사용자별 장바구니 조회 (삭제되지 않은 것만)
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND deleted_at IS NULL")
    @Results(id = "cartMap", value = {
            @Result(column = "cart_id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "deleted_at", property = "deletedAt")
    })
    Cart findByUserId(Long userId);

    // 장바구니 ID로 조회 (삭제되지 않은 것만)
    @Select("SELECT * FROM cart WHERE cart_id = #{cartId} AND deleted_at IS NULL")
    @ResultMap("cartMap")
    Cart findById(String cartId);

    // 장바구니 생성
    @Insert("INSERT INTO cart (cart_id, user_id, created_at, updated_at) " +
            "VALUES (#{id}, #{userId}, NOW(), NOW())")
    void insertCart(Cart cart);

    // 장바구니 업데이트 (최종 수정일시)
    @Update("UPDATE cart SET updated_at = NOW() WHERE cart_id = #{cartId} AND deleted_at IS NULL")
    void updateCart(String cartId);

    // 장바구니 소프트 삭제
    @Update("UPDATE cart SET deleted_at = NOW(), updated_at = NOW() WHERE cart_id = #{cartId}")
    void softDeleteCart(String cartId);

    // 사용자별 장바구니 소프트 삭제
    @Update("UPDATE cart SET deleted_at = NOW(), updated_at = NOW() WHERE user_id = #{userId}")
    void softDeleteByUserId(Long userId);

    // 장바구니 복구
    @Update("UPDATE cart SET deleted_at = NULL, updated_at = NOW() WHERE cart_id = #{cartId}")
    void restoreCart(String cartId);

    // 사용자별 장바구니와 아이템들 함께 조회
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND deleted_at IS NULL")
    @Results(id = "cartWithItemsMap", value = {
            @Result(column = "cart_id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "deleted_at", property = "deletedAt"),
            @Result(column = "cart_id", property = "cartItemList",
                    many = @Many(select = "com.fast_campus_12.not_found.shop.mapper.CartItemMapper.findItemsByCartId"))
    })
    Cart findCartWithItemsByUserId(Long userId);


    @Select("SELECT * FROM cart_item WHERE cart_id = #{cartId}")
    @Results(id = "cartItemMap", value = {
            @Result(column = "card_item_id", property = "id"),
            @Result(column = "cart_id", property = "cartId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "product_v_id", property = "productVariantId"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    List<CartItem> findItemsByCartId(Long cartId);

    //CartItemViewDto로 조회
    @Select("""
        SELECT 
            ci.cart_item_id,
            ci.product_variant_id AS productVariantId,
            p.product_title AS productName,
            pv.product_variant_color_id AS color,
            pv.product_variant_size_id AS size,
            p.product_thumbnail AS image_url,
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
    
}
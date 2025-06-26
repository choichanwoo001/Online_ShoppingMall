package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.dto.CartItemViewDto;
import com.fast_campus_12.not_found.shop.product.model.Cart;
import com.fast_campus_12.not_found.shop.product.model.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    // 사용자별 장바구니 조회 (삭제되지 않은 것만)
    @Select("SELECT * FROM CART WHERE USER_ID = #{userId} AND DELETED_AT IS NULL")
    @Results(id = "cartMap", value = {
            @Result(column = "CART_ID", property = "id"),
            @Result(column = "USER_ID", property = "userId"),
            @Result(column = "CREATED_AT", property = "createdAt"),
            @Result(column = "UPDATED_AT", property = "updatedAt"),
            @Result(column = "DELETED_AT", property = "deletedAt")
    })
    Cart findByUserId(Long userId);

    // 장바구니 ID로 조회 (삭제되지 않은 것만)
    @Select("SELECT * FROM CART WHERE CART_ID = #{cartId} AND DELETED_AT IS NULL")
    @ResultMap("cartMap")
    Cart findById(String cartId);

    // 장바구니 생성
    @Insert("INSERT INTO CART (CART_ID, USER_ID, CREATED_AT, UPDATED_AT) " +
            "VALUES (#{id}, #{userId}, NOW(), NOW())")
    void insertCart(Cart cart);

    // 장바구니 업데이트 (최종 수정일시)
    @Update("UPDATE CART SET UPDATED_AT = NOW() WHERE CART_ID = #{cartId} AND DELETED_AT IS NULL")
    void updateCart(String cartId);

    // 장바구니 소프트 삭제
    @Update("UPDATE CART SET DELETED_AT = NOW(), UPDATED_AT = NOW() WHERE CART_ID = #{cartId}")
    void softDeleteCart(String cartId);

    // 사용자별 장바구니 소프트 삭제
    @Update("UPDATE CART SET DELETED_AT = NOW(), UPDATED_AT = NOW() WHERE USER_ID = #{userId}")
    void softDeleteByUserId(Long userId);

    // 장바구니 복구
    @Update("UPDATE CART SET DELETED_AT = NULL, UPDATED_AT = NOW() WHERE CART_ID = #{cartId}")
    void restoreCart(String cartId);

    // 사용자별 장바구니와 아이템들 함께 조회
    @Select("SELECT * FROM CART WHERE USER_ID = #{userId} AND DELETED_AT IS NULL")
    @Results(id = "cartWithItemsMap", value = {
            @Result(column = "CART_ID", property = "id"),
            @Result(column = "USER_ID", property = "userId"),
            @Result(column = "CREATED_AT", property = "createdAt"),
            @Result(column = "UPDATED_AT", property = "updatedAt"),
            @Result(column = "DELETED_AT", property = "deletedAt"),
            @Result(column = "CART_ID", property = "cartItemList",
                    many = @Many(select = "com.fast_campus_12.not_found.shop.mapper.CartItemMapper.findItemsByCartId"))
    })
    Cart findCartWithItemsByUserId(Long userId);

    @Select("SELECT * FROM CART_ITEM WHERE CART_ID = #{cartId}")
    @Results(id = "cartItemMap", value = {
            @Result(column = "CART_ITEM_ID", property = "id"),
            @Result(column = "CART_ID", property = "cartId"),
            @Result(column = "PRODUCT_ID", property = "productId"),
            @Result(column = "PRODUCT_VARIANT_ID", property = "productVariantId"),
            @Result(column = "QUANTITY", property = "quantity"),
            @Result(column = "UPDATED_AT", property = "updatedAt")
    })
    List<CartItem> findItemsByCartId(Long cartId);

    // CartItemViewDto로 조회
    @Select("""
        SELECT 
            CI.CART_ITEM_ID,
            CI.PRODUCT_VARIANT_ID AS productVariantId,
            P.PRODUCT_TITLE AS productName,
            PV.PRODUCT_VARIANT_COLOR_ID AS color,
            PV.PRODUCT_VARIANT_SIZE_ID AS size,
            P.PRODUCT_THUMBNAIL AS image_url,
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
}
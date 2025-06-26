package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.WishList;
import com.fast_campus_12.not_found.shop.dto.WishlistItemDto;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface WishlistMapper {

    @Select("""
        SELECT 
            w.WISH_ID as wish_id,
            w.USER_ID as user_id,
            w.PRODUCT_ID as product_id,
            w.ADDED_AT as added_at,
            p.PRODUCT_TITLE as title,
            p.PRODUCT_SUMMARY as summary,
            p.PRODUCT_THUMBNAIL as thumbnail,
            p.PRODUCT_PRICE as price,
            p.PRODUCT_STOCK as stock_quantity,
            p.PRODUCT_AVERAGE_RATING as average_rating,
            p.PRODUCT_TAG as tags,
            p.IS_ENABLED as enabled,
            CONCAT(l1.NAME, ' > ', l2.NAME, ' > ', l3.NAME) as category_name
        FROM WISH_LIST w
        INNER JOIN PRODUCT p ON w.PRODUCT_ID = p.PRODUCT_ID
        LEFT JOIN CATEGORY_LV3 l3 ON p.CATEGORY_LV3_ID = l3.CATEGORY_LV3_ID
        LEFT JOIN CATEGORY_LV2 l2 ON l3.CATEGORY_LV2_ID = l2.CATEGORY_LV2_ID
        LEFT JOIN CATEGORY_LV1 l1 ON l2.CATEGORY_LV1_ID = l1.CATEGORY_LV1_ID
        WHERE w.USER_ID = #{userId}
        AND p.IS_ENABLED = 1
        AND p.DELETED_AT IS NULL
        ORDER BY w.ADDED_AT DESC
        LIMIT #{size} OFFSET #{offset}
    """)
    List<WishlistItemDto> selectWishlistByUserId(@Param("userId") Long userId,
                                                 @Param("offset") int offset,
                                                 @Param("size") int size);

    @Select("""
        SELECT COUNT(*)
        FROM WISH_LIST w
        INNER JOIN PRODUCT p ON w.PRODUCT_ID = p.PRODUCT_ID
        WHERE w.USER_ID = #{userId}
        AND p.IS_ENABLED = 1
        AND p.DELETED_AT IS NULL
    """)
    int countWishlistByUserId(@Param("userId") Long userId);

    @Select("""
        SELECT COUNT(*) > 0
        FROM WISH_LIST
        WHERE USER_ID = #{userId} AND PRODUCT_ID = #{productId}
    """)
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Insert("""
        INSERT INTO WISH_LIST (USER_ID, PRODUCT_ID, ADDED_AT)
        VALUES (#{userId}, #{productId}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "wishId")
    int insertWishlist(WishList wishList);

    @Delete("DELETE FROM WISH_LIST WHERE WISH_ID = #{wishId}")
    int deleteByWishId(@Param("wishId") Long wishId);

    @Delete("DELETE FROM WISH_LIST WHERE USER_ID = #{userId} AND PRODUCT_ID = #{productId}")
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Delete("""
        <script>
        DELETE FROM WISH_LIST 
        WHERE WISH_ID IN
        <foreach collection="wishIds" item="wishId" open="(" close=")" separator=",">
            #{wishId}
        </foreach>
        </script>
    """)
    int deleteByWishIds(@Param("wishIds") List<Long> wishIds);

    @Delete("DELETE FROM WISH_LIST WHERE USER_ID = #{userId}")
    int deleteAllByUserId(@Param("userId") Long userId);
}
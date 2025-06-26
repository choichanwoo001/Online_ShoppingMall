package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.entity.WishList;
import com.fast_campus_12.not_found.shop.dto.WishlistItemDto;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface WishlistMapper {

    @Select("""
        SELECT 
            w.wish_id,
            w.user_id,
            w.product_id,
            w.added_at,
            p.title,
            p.summary,
            p.thumbnail,
            p.price,
            p.discount_price,
            p.stock_quantity,
            p.average_rating,
            p.tags,
            p.enabled,
            CONCAT(l1.name, ' > ', l2.name, ' > ', l3.name) as category_name,
            (SELECT COUNT(*) FROM review r WHERE r.product_id = p.product_id AND r.state = 'approved') as review_count
        FROM WISH_LIST w
        INNER JOIN product p ON w.product_id = p.product_id
        LEFT JOIN lv3 ON p.lv3_id = lv3.lv3_id
        LEFT JOIN lv2 ON lv3.lv2_id = lv2.lv2_id
        LEFT JOIN lv1 ON lv2.lv1_id = lv1.lv1_id
        WHERE w.user_id = #{userId}
        AND p.enabled = 1
        AND p.deleted_at IS NULL
        ORDER BY w.added_at DESC
        LIMIT #{size} OFFSET #{offset}
    """)
    List<WishlistItemDto> selectWishlistByUserId(@Param("userId") String userId,
                                                 @Param("offset") int offset,
                                                 @Param("size") int size);

    @Select("""
        SELECT COUNT(*)
        FROM WISH_LIST w
        INNER JOIN product p ON w.product_id = p.product_id
        WHERE w.user_id = #{userId}
        AND p.enabled = 1
        AND p.deleted_at IS NULL
    """)
    int countWishlistByUserId(@Param("userId") String userId);

    @Select("""
        SELECT COUNT(*) > 0
        FROM WISH_LIST
        WHERE user_id = #{userId} AND product_id = #{productId}
    """)
    boolean existsByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Long productId);

    @Insert("""
        INSERT INTO WISH_LIST (user_id, product_id, added_at)
        VALUES (#{userId}, #{productId}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "wishId")
    int insertWishlist(WishList wishList);

    @Delete("DELETE FROM WISH_LIST WHERE wish_id = #{wishId}")
    int deleteByWishId(@Param("wishId") Long wishId);

    @Delete("DELETE FROM WISH_LIST WHERE user_id = #{userId} AND product_id = #{productId}")
    int deleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Long productId);

    @Delete("""
        <script>
        DELETE FROM WISH_LIST 
        WHERE wish_id IN
        <foreach collection="wishIds" item="wishId" open="(" close=")" separator=",">
            #{wishId}
        </foreach>
        </script>
    """)
    int deleteByWishIds(@Param("wishIds") List<Long> wishIds);

    @Delete("DELETE FROM WISH_LIST WHERE user_id = #{userId}")
    int deleteAllByUserId(@Param("userId") String userId);
}
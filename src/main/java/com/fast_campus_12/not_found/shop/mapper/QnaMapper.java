package com.fast_campus_12.not_found.shop.mapper;


import com.fast_campus_12.not_found.shop.qna.dto.QnaDto;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface QnaMapper {

    @Select("""
        SELECT INQUIRY_ID,
               USER_ID,
               PRODUCT_ID,
               CONTENT,
               IS_SECRET,
               CREATED_AT         
        FROM INQUIRY
        WHERE DELETED_AT IS NULL
        ORDER BY CREATED_AT DESC
    """)
    List<QnaDto> getAllQnas();

    @Select("""
        SELECT INQUIRY_ID,
               USER_ID,
               PRODUCT_ID,
               CONTENT,
               IS_SECRET,
               CREATED_AT         
        FROM INQUIRY
        WHERE USER_ID = #{userId}
          AND DELETED_AT IS NULL
        ORDER BY CREATED_AT DESC
    """)
    List<QnaDto> getQnaByUserId(@Param("userId") BigInteger userId);
};


package com.fast_campus_12.not_found.shop.mapper;


import com.fast_campus_12.not_found.shop.qna.dto.QnaDto;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BinaryOperator;

@Mapper
public interface QnaMapper {
    @Select("""
                select INQUIRY_ID,
                       USER_ID,
                       PRODUCT_ID,
                       CONTENT,
                       IS_SECRET,
                       CREATED_AT         
                from INQUIRY
                where DELETED_AT is null 
                """)List<QnaDto> getAllQnas();
    @Select("""
                select INQUIRY_ID,
                       USER_ID,
                       PRODUCT_ID,
                       CONTENT,
                       IS_SECRET,
                       CREATED_AT
                from INQUIRY
                where 
                """)List<QnaDto>getQnaByUserId(BigInteger userId);



};


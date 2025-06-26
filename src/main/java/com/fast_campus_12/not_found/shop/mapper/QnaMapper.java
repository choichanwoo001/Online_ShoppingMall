//package com.fast_campus_12.not_found.shop.mapper;
//
//import com.fast_campus_12.not_found.shop.qna.dto.QnaDto;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//import java.math.BigInteger;
//import java.util.List;
//
//@Mapper
//public interface QnaMapper {
//
//    @Select("""
//            select *
//            from INQUIRY
//            where USER_ID = #{userId} and IS_SECRET = 0
//            ORDER BY CREATED_AT
//            """)
//    List<QnaDto>findByUserId(BigInteger userId);
//
//    @Select("""
//            select TITLE,
//                   USER_ID,
//                   PRODUCT_ID,
//                   IS_SECRET,
//                   CREATED_AT
//            from INQUIRY
//            order by CREATED_AT
//            """)
//    List<QnaDto>ListOfQna();
//}
//
//

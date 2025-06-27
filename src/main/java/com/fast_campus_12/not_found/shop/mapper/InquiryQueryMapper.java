package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.domain.inquiry.dto.InquiryDto;
import com.fast_campus_12.not_found.shop.domain.inquiry.dto.request.InquiryPageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryQueryMapper {
    List<InquiryDto> findInquiryList(@Param("request") InquiryPageRequest request);
    int countInquiryList(@Param("request") InquiryPageRequest request);
}

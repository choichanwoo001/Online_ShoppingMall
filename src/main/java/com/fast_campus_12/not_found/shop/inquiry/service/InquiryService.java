package com.fast_campus_12.not_found.shop.inquiry.service;

import com.fast_campus_12.not_found.shop.inquiry.dto.InquiryPageDto;
import com.fast_campus_12.not_found.shop.inquiry.dto.request.InquiryPageRequest;
import com.fast_campus_12.not_found.shop.mapper.InquiryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryQueryMapper inquiryQueryMapper;

    public InquiryPageDto findPinnedInquiry() {
        return null;
//                InquiryPageDto.builder()
//                        .items(inquiryQueryMapper.find())
//                        .build();
    }

    public InquiryPageDto findUnpinnedInquiries(InquiryPageRequest pageRequest) {
        return InquiryPageDto.builder()
                .totalCount(inquiryQueryMapper.countInquiryList(pageRequest))
                .items(inquiryQueryMapper.findInquiryList(pageRequest))
                .build();
    }
}

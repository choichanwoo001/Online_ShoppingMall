package com.fast_campus_12.not_found.shop.inquiry.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InquiryPageDto {
    private int totalCount;
    private List<InquiryDto> items;
}

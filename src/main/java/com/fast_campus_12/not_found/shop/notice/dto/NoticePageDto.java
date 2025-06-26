package com.fast_campus_12.not_found.shop.notice.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NoticePageDto {
    private int totalCount;
    private List<NoticeDto> items;
}

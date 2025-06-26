package com.fast_campus_12.not_found.shop.inquiry.dto.request;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.inquiry.enums.InquirySortBy;
import com.fast_campus_12.not_found.shop.notice.enums.NoticeSortBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryPageRequest extends PageRequestDto<InquirySortBy> {
    private int limit = 10;

    public int getOffset()  {
        return super.getOffset(limit);
    }

    public InquiryPageRequest() {
        setPage(1);
        setSortBy(InquirySortBy.CREATED_AT);
        setSort(SortDirection.DESC);
    }
}
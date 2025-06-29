package com.fast_campus_12.not_found.shop.domain.notice.dto.request;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import com.fast_campus_12.not_found.shop.global.dto.pagination.PageRequestDto;
import com.fast_campus_12.not_found.shop.domain.notice.enums.NoticeSortBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticePageRequest extends PageRequestDto<NoticeSortBy> {
    private int limit = 10;

    public int getOffset()  {
        return super.getOffset(limit);
    }

    public NoticePageRequest() {
        setPage(1);
        setSortBy(NoticeSortBy.CREATED_AT);
        setSort(SortDirection.DESC);
    }
}
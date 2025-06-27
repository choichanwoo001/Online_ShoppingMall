package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.domain.notice.dto.NoticeDto;
import com.fast_campus_12.not_found.shop.domain.notice.dto.request.NoticePageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeQueryMapper {
    List<NoticeDto> findNoticeList(@Param("request") NoticePageRequest request);
    int countNoticeList(@Param("request") NoticePageRequest request);

    List<NoticeDto> findPinnedNotices();
}

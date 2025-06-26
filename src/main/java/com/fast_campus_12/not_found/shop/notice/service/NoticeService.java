package com.fast_campus_12.not_found.shop.notice.service;

import com.fast_campus_12.not_found.shop.mapper.NoticeDynamicQueryMapper;
import com.fast_campus_12.not_found.shop.notice.dto.NoticeDto;
import com.fast_campus_12.not_found.shop.notice.dto.NoticePageDto;
import com.fast_campus_12.not_found.shop.notice.dto.request.NoticePageRequest;
import com.fast_campus_12.not_found.shop.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeDynamicQueryMapper noticeDynamicQueryMapper;

    public List<NoticeDto> findAllNotices() {
        return noticeRepository.findAll();
    }

    public NoticePageDto paging(NoticePageRequest noticePageRequest) {
        return NoticePageDto.builder()
                .totalCount(noticeDynamicQueryMapper.countNoticeList(noticePageRequest))
                .items(noticeDynamicQueryMapper.findNoticeList(noticePageRequest))
                .build();
    }

    public Optional<NoticeDto> findNoticeById(Long id) {
        return noticeRepository.findById(id);
    }
}

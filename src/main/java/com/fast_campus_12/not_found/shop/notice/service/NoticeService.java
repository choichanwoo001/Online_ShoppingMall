package com.fast_campus_12.not_found.shop.notice.service;

import com.fast_campus_12.not_found.shop.mapper.NoticeQueryMapper;
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
    private final NoticeQueryMapper noticeQueryMapper;

    public List<NoticeDto> findAllNotices() {
        return noticeRepository.findAll();
    }

    public NoticePageDto findPinnedNotices() {
        return
        NoticePageDto.builder()
                .items(noticeQueryMapper.findPinnedNotices())
                .build();
    }


    public NoticePageDto findUnpinnedNotices(NoticePageRequest noticePageRequest) {
        return NoticePageDto.builder()
                .totalCount(noticeQueryMapper.countNoticeList(noticePageRequest))
                .items(noticeQueryMapper.findNoticeList(noticePageRequest))
                .build();
    }

    public Optional<NoticeDto> findNoticeById(Long id) {
        return noticeRepository.findById(id);
    }
}

package com.fast_campus_12.not_found.shop.notice.service;

import com.fast_campus_12.not_found.shop.notice.dto.NoticeDto;
import com.fast_campus_12.not_found.shop.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeRepository NoticeRepository;

    public NoticeService(NoticeRepository NoticeRepository) {
        this.NoticeRepository = NoticeRepository;
    }

    public List<NoticeDto> findAllNotices() {
        return NoticeRepository.findAll();
    }

    public Optional<NoticeDto> findNoticeById(Long id) {
        return NoticeRepository.findById(id);
    }
}

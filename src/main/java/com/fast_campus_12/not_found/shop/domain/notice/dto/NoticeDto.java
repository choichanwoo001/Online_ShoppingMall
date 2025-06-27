package com.fast_campus_12.not_found.shop.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int viewCount;
    private LocalDateTime createdAt;
    private boolean isPinned;

}



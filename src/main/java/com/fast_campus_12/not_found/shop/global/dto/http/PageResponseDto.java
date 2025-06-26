package com.fast_campus_12.not_found.shop.global.dto.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 페이징 응답 DTO
 * <p>
 * 목록 조회 결과와 페이징 정보를 포함하여 응답하는 데 사용
 *
 * @author : jys01012@gmail.com
 * @version : 1.0
 *
 * @param <T> 목록 아이템 타입
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {

    /** 현재 페이지에 포함된 아이템 목록 */
    private List<T> items;

    /** 전체 데이터 수 */
    private int totalCount;

    /** 현재 페이지 번호 (1부터 시작) */
    private int page;

    /** 페이지당 아이템 수 */
    private int size;

    /** 다음 페이지 존재 여부 */
    private boolean hasNext;

    /** 이전 페이지 존재 여부 */
    private boolean hasPrev;

    /**
     * 전체 페이지 수 계산
     *
     * @return 전체 페이지 수
     */
    public int getTotalPages() {
        if (size == 0) return 0;
        return (int) Math.ceil((double) totalCount / size);
    }

    /**
     * PageResponseDto 생성 유틸 메서드
     *
     * @param items 현재 페이지 아이템 목록
     * @param totalCount 전체 아이템 수
     * @param page 현재 페이지 번호
     * @param size 페이지당 아이템 수
     * @return 구성된 PageResponseDto 인스턴스
     * @param <T> 아이템 타입
     */
    public static <T> PageResponseDto<T> of(List<T> items, int totalCount, int page, int size) {
        return PageResponseDto.<T>builder()
                .items(items)
                .totalCount(totalCount)
                .page(page)
                .size(size)
                .hasNext(totalCount > page * size)
                .hasPrev(page > 1)
                .build();
    }
}

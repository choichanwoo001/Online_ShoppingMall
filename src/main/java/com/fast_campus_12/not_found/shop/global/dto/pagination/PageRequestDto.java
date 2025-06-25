package com.fast_campus_12.not_found.shop.global.dto.pagination;

import com.fast_campus_12.not_found.shop.common.enums.SortDirection;
import lombok.Getter;
import lombok.Setter;

/**
 * 페이징 요청 정보를 담는 DTO.
 * <p>
 * 페이지 번호, 정렬 기준, 정렬 방향 등의 정보를 포함
 *
 * @param <SORT_BY> Enum 타입의 정렬 기준
 *
 * @author jys01012@gmail.com
 * @version 1.0
 */
@Getter
@Setter
public class PageRequestDto<SORT_BY extends Enum<SORT_BY>> {

    /**
     * 요청 페이지 번호 (기본값: 1)
     */
    private int page = 1;

    /**
     * 정렬 기준 (Enum 타입)
     */
    private SORT_BY sortBy;

    /**
     * 정렬 방향 (ASC, DESC)
     */
    private SortDirection sort;

    /**
     * 페이지네이션을 위한 데이터 조회 오프셋 반환
     *
     * @param size 페이지당 아이템 수
     * @return 오프셋 값
     */
    public int getOffset(int size) {
        return (Math.max(page, 1) - 1) * size;
    }
}

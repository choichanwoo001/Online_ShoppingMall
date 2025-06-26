package com.fast_campus_12.not_found.shop.notice.enums;

import lombok.Getter;

@Getter
public enum NoticeSortBy {
    CREATED_AT("CREATED_AT");

    private final String column;

    NoticeSortBy(String column) {
        this.column = column;
    }

}

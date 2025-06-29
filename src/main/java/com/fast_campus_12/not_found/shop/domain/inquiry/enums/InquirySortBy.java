package com.fast_campus_12.not_found.shop.domain.inquiry.enums;

import lombok.Getter;

@Getter
public enum InquirySortBy {
    CREATED_AT("CREATED_AT");

    private final String column;

    InquirySortBy(String column) {
        this.column = column;
    }

}

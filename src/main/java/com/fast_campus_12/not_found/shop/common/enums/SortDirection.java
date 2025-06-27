package com.fast_campus_12.not_found.shop.common.enums;

public enum SortDirection {
    ASC, DESC;

    public static SortDirection from(String value) {
        return SortDirection.valueOf(value.toUpperCase());
    }
}

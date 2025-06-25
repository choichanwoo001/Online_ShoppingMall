package com.fast_campus_12.not_found.shop.common.enums;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
public class SortDirectionConverter implements Converter<String, SortDirection> {
    @Override
    public SortDirection convert(String source) {
        log.warn(source);
        return SortDirection.valueOf(source.toUpperCase());
    }
}
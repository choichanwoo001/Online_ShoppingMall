package com.fast_campus_12.not_found.shop.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Objects;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    @SuppressWarnings("unchecked")
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private static class StringToEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (Objects.isNull(source) || source.isBlank()) return null;
            for (T constant : enumType.getEnumConstants()) {
                if (constant.name().equalsIgnoreCase(source.trim())) {
                    return constant;
                }
            }
            throw new IllegalArgumentException(
                    "No enum constant " + enumType.getSimpleName() + " for value: " + source
            );
        }

    }
}

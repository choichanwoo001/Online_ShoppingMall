    package com.fast_campus_12.not_found.shop.domain.product.enums;

    import lombok.Getter;

    @Getter
    public enum ProductSortBy {
        PRICE("PRODUCT_PRICE"),
        NAME("PRODUCT_TITLE"),
        SALE_COUNT("PRODUCT_SALE_COUNT"),
        CREATED_AT("CREATED_AT");

        private final String column;

        ProductSortBy(String column) {
            this.column = column;
        }

    }
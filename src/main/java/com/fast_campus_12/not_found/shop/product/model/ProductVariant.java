package com.fast_campus_12.not_found.shop.product.model;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;


@Data
public class ProductVariant {

    private BigInteger productVariantId;

    private BigInteger productId;

    private Integer colorId;

    private String sku;

    private String size;

    private Integer remainingStock;

    private StockStatus stockStatus;

    private Double priceModifier;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
enum StockStatus {
    IN_STOCK, OUT_OF_STOCK, LOW_STOCK
}

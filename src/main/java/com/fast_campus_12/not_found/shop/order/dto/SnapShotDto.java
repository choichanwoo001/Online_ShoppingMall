package com.fast_campus_12.not_found.shop.order.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SnapShotDto {
    private Long productSnapshotId;
    private Long productId;
    private String title;
    private double price;
    private double discountPrice;
    private String sku;
    private String summary;
    private String description;
    private String thumbnail;
    private double weight;
    private String dimensions;
    private String special;
    private String tags;
    private String seasons;
    private Boolean isFeatured;
    private double averageRating;
    private Integer salesCount;
    private Integer viewCount;
    private Integer stockQuantity;
    private Date availabilityDate;
    private Boolean enabled;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Date isDeleted;
    private String hexCode;
    private String colorName;
    private String size;
    private String fabricInfo;
    private String careInstructions;
    private double originalPrice;
    private double extraCharge;
    private double salePrice;
    private double discountRate;

    public SnapShotDto(double price, String title) {
        this.price = price;
        this.title = title;
    }
}

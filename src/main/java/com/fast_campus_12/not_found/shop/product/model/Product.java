package com.fast_campus_12.not_found.shop.product.model;

import com.fast_campus_12.not_found.shop.product.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String title;
    private int price;
    private int discount_price;
    private String summary;
    private String description;
    private String thumbnail;
    private int weight;
    private int dimension;
    private String tag;
    private String season;
    private double averageRating;
    private int salesCount;
    private int viewCount;
    private String stockQuantity;
    private Date availableDate;
    private boolean enabled;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private boolean isDeleted;
    private String comment;

    private Category category;
}

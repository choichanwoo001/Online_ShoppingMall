package com.fast_campus_12.not_found.shop.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderHistoryModel {
    private Long historyId;
    private Long orderId;
    private String status;
    private String comment;
    private LocalDateTime createdAt;
    private String createdBy;
    private Long id2;
}

package com.fast_campus_12.not_found.shop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistPageDto {
    private List<WishlistItemDto> content;
    private Integer currentPage;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Boolean success;
    private String message;
}

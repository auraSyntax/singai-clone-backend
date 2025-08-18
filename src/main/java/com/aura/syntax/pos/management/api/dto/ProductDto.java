package com.aura.syntax.pos.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String productName;
    private Integer currentStock;
    private Integer minimumStock;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;

    private Long categoryId;
    private String imageUrl;
    private String description;
    private Double price;
}

package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private Integer quantity;
    private Double costPerUnit;
    private String unit;
    private Double salesPrice;
    private Double retailPrice;
    private Boolean isMinimumStock;

    public ProductDto(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public ProductDto(Long id, String productName, Integer currentStock, Integer minimumStock, Type type, Boolean isMinimumStock) {
        this.id = id;
        this.productName = productName;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.type = type != null ? type.getMappedValue() : null;
        this.isMinimumStock = isMinimumStock;
    }
}

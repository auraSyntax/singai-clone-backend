package com.aura.syntax.pos.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockDto {
    private Long id;
    private String stockName;
    private Double currentStock;
    private Double minimumStock;
    private Double costPerUnit;
    private String unit;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockDto(Long id, String stockName, String unit, Double currentStock, Double minimumStock, Double costPerUnit) {
        this.id = id;
        this.stockName = stockName;
        this.unit = unit;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.costPerUnit = costPerUnit;
    }
}

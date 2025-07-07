package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.entity.MenuItemIncredients;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientsDto {
    private Long id;
    private String name;
    private String unit;
    private Integer currentStock;
    private Integer minimumStock;
    private Double costPerUnit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    Set<MenuItemIncredients> menuItemIncredients;

    public IngredientsDto(Long id, String name, String unit, Integer currentStock, Integer minimumStock, Double costPerUnit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.costPerUnit = costPerUnit;
    }
}

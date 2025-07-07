package com.aura.syntax.pos.management.api.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MenuItemIncredientsDto {
    private Long id;
    private Long menuItemsId;
    private Long ingredientsId;
    private Integer quantityRequired;
    private String unit;
    private LocalDateTime createdAt;
}

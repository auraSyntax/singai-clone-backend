package com.aura.syntax.pos.management.api.dto;

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
    private Long productId;
    private Integer quantityRequired;
    private String unit;
    private LocalDateTime createdAt;
}

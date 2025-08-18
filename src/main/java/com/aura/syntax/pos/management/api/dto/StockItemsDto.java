package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class StockItemsDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double costPerUnit;
    private String unit;
    private Double salesPrice;
    private Double retailPrice;
    private Long menuItemId;
    private Boolean isActive;

//    public StockItemsDto(Long id, String name, Integer currentStock, Integer minimumStock, Type type, Long menuItemId, String unit, Boolean isActive) {
//        this.id = id;
//        this.name = name;
//        this.currentStock = currentStock;
//        this.minimumStock = minimumStock;
//        this.type = type.getMappedValue();
//        this.menuItemId = menuItemId;
//        this.unit = unit;
//        this.isActive = isActive;
//    }
}

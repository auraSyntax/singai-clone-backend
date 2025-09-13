package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private String productName;

    public StockItemsDto(Long id, Integer quantity, Double costPerUnit,
                         String unit, Double salesPrice,
                         Double retailPrice, String productName) {
        this.id = id;
        this.quantity = quantity;
        this.costPerUnit = costPerUnit;
        this.unit = unit;
        this.salesPrice = salesPrice;
        this.retailPrice = retailPrice;
        this.productName = productName;
    }
}

package com.aura.syntax.pos.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class StockDto {
    private Long id;
    private LocalDateTime dateTime;
    private Double total;
    private String invoiceNumber;
    private Boolean isActive;
    private Set<StockItemsDto> stockItemsDtos;

    public StockDto(Long id, Boolean isActive) {
        this.id = id;
        this.isActive = isActive;
    }
}

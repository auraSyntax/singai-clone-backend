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
    private String stockName;
    private LocalDateTime dateTime;
    private Double total;
    private String invoiceNumber;
    private Boolean isActive;
    private Set<StockItemsDto> stockItemsDtos;
    private Set<ProductDto> productDtos;

    public StockDto(Long id, String stockName, String invoiceNumber, Double total) {
        this.id = id;
        this.stockName = stockName;
        this.invoiceNumber = invoiceNumber;
        this.total = total;
    }
}

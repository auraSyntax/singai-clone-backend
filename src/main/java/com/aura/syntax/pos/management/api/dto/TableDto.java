package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.TableStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableDto {
    private Long id;
    private String tableNumber;
    private Integer capacity;
    private String tableStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TableDto(Long id, String tableNumber, Integer capacity, TableStatus tableStatus) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.tableStatus = tableStatus.getMappedValue();
    }
}

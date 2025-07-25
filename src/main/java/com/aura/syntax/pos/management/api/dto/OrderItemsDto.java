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
public class OrderItemsDto {
    private Long id;
    private Long orderId;
    private Long menuItemsId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String status;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

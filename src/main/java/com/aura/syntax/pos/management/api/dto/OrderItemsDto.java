package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.OrderStatus;
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
    private String menuItemName;

    public OrderItemsDto(Long id, Long menuItemsId, Integer quantity, String specialInstructions, OrderStatus orderStatus) {
        this.id = id;
        this.menuItemsId = menuItemsId;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.status = orderStatus != null ? orderStatus.getMappedValue() : null;
    }
}

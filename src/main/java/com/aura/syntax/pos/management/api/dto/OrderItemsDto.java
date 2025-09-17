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
public class OrderItemsDto {
    private Long id;
    private Long orderId;
    private Long menuItemsId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String status;
    private String specialInstructions;
    private String createdAt;
    private String updatedAt;
    private String menuItemName;

    private Double menuItemPrice;
    private String imageUrl;
    private Integer preparationTime;
    private Boolean isRetail;

    public OrderItemsDto(Long id, Long menuItemsId, Integer quantity, String specialInstructions, OrderStatus orderStatus,String name,
                         Double unitPrice,Double totalPrice,LocalDateTime createdAt,
                         LocalDateTime updatedAt,Double price,String imageUrl,Integer preparationTime, Boolean isRetail) {
        this.id = id;
        this.menuItemsId = menuItemsId;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.status = orderStatus != null ? orderStatus.getMappedValue() : null;
        this.menuItemName = name;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt != null ? createdAt.toString() : null;
        this.updatedAt = updatedAt != null ? updatedAt.toString() : null;
        this.menuItemPrice = price;
        this.imageUrl = imageUrl;
        this.preparationTime = preparationTime;
        this.isRetail = isRetail;
    }

    public OrderItemsDto(Long id, Long menuItemsId, Integer quantity, String specialInstructions, OrderStatus orderStatus,String name,
                         Double unitPrice,Double totalPrice,Double price,String imageUrl,Integer preparationTime, Boolean isRetail) {
        this.id = id;
        this.menuItemsId = menuItemsId;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.status = orderStatus != null ? orderStatus.getMappedValue() : null;
        this.menuItemName = name;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.menuItemPrice = price;
        this.imageUrl = imageUrl;
        this.preparationTime = preparationTime;
        this.isRetail = isRetail;
    }

    public OrderItemsDto(Long id, Integer quantity, Double unitPrice) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}

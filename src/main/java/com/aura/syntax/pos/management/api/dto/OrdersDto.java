package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.OrderStatus;
import com.aura.syntax.pos.management.enums.OrderType;
import com.aura.syntax.pos.management.enums.PaymentMethod;
import com.aura.syntax.pos.management.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    private Long id;
    private Long tableId;
    private Long waiterId;
    private String customerName;
    private String customerPhone;
    private String orderType;
    private String orderStatus;
    private Double subTotal;
    private Double taxAmount;
    private Double discountAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    private String orderNumber;
    private Set<OrderItemsDto> orderItemsDtos;
    private String tableName;
    private String waiterName;
    private Double totalAmount;
    private Integer orderPreparationTime;
    private String createdAt;
    private String updatedAt;

    public OrdersDto(Long id, String orderNumber, Long tableId, Long waiterId, OrderType orderType, OrderStatus orderStatus,
                     PaymentMethod paymentMethod, PaymentStatus paymentStatus, String customerName, String customerPhone, String notes, Double subTotal,
                     Double taxAmount, Double discountAmount, String tableName, String waiterName,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.tableId = tableId;
        this.waiterId = waiterId;
        this.orderType = orderType != null ? orderType.getMappedValue() : null;
        this.orderStatus = orderStatus != null ? orderStatus.getMappedValue() : null;
        this.paymentMethod = paymentMethod != null ? paymentMethod.getMappedValue() : null;
        this.paymentStatus = paymentStatus != null ? paymentStatus.getMappedValue() : null;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.notes = notes;
        this.subTotal = subTotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.tableName = tableName;
        this.waiterName = waiterName;
        this.createdAt = createdAt != null ? createdAt.toString() : null;
        this.updatedAt = updatedAt != null ? updatedAt.toString() : null;
    }

    public OrdersDto(Long id, Double subTotal, Double taxAmount, Double discountAmount,PaymentMethod paymentMethod) {
        this.id = id;
        this.subTotal = subTotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.paymentMethod = paymentMethod != null ? paymentMethod.getMappedValue() : null;
    }
}

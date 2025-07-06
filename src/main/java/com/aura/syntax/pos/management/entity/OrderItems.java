package com.aura.syntax.pos.management.entity;

import com.aura.syntax.pos.management.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long menuItemsId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

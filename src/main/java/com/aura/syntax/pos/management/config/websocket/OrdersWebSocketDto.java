package com.aura.syntax.pos.management.config.websocket;

import com.aura.syntax.pos.management.enums.OrderStatus;
import com.aura.syntax.pos.management.enums.OrderType;
import lombok.Data;

@Data
public class OrdersWebSocketDto {
    private Long id;
    private String orderNumber;
    private Double totalPrice;
    private Integer preparationTime;
    private String orderType;
    private String orderStatus;

    public OrdersWebSocketDto(Long id, String orderNumber, OrderType orderType, OrderStatus orderStatus) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderType = orderType != null ? orderType.getMappedValue() : null;
        this.orderStatus = orderStatus != null ? orderStatus.getMappedValue() : null;
    }
}

package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready"),
    SERVED("Served"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String mappedValue;

    OrderStatus(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static OrderStatus fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.mappedValue.equalsIgnoreCase(mappedValue)) {
                return orderStatus;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            list.add(orderStatus.mappedValue);
        }
        return list;
    }

    public static List<OrderStatus> getAllEnum() {
        List<OrderStatus> list = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            list.add(orderStatus);
        }
        return list;
    }
}

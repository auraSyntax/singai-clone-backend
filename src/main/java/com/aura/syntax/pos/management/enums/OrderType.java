package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum OrderType {
    DINE_IN("Dine In"),
    TAKE_AWAY("Take Away"),
    DELIVERY("Delivery");

    private final String mappedValue;

    OrderType(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static OrderType fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (OrderType orderType : OrderType.values()) {
            if (orderType.mappedValue.equalsIgnoreCase(mappedValue)) {
                return orderType;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (OrderType orderType : OrderType.values()) {
            list.add(orderType.mappedValue);
        }
        return list;
    }

    public static List<OrderType> getAllEnum() {
        List<OrderType> list = new ArrayList<>();
        for (OrderType orderType : OrderType.values()) {
            list.add(orderType);
        }
        return list;
    }
}

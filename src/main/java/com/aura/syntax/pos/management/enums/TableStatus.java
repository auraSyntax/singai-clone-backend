package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum TableStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    RESERVED("Reserved"),
    MAINTENANCE("Maintenance");

    private final String mappedValue;

    TableStatus(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static TableStatus fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (TableStatus tableStatus : TableStatus.values()) {
            if (tableStatus.mappedValue.equalsIgnoreCase(mappedValue)) {
                return tableStatus;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (TableStatus tableStatus : TableStatus.values()) {
            list.add(tableStatus.mappedValue);
        }
        return list;
    }

    public static List<TableStatus> getAllEnum() {
        List<TableStatus> list = new ArrayList<>();
        for (TableStatus tableStatus : TableStatus.values()) {
            list.add(tableStatus);
        }
        return list;
    }
}

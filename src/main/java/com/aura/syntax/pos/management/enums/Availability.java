package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum Availability {
    AVAILABLE("Available"),
    OUT_OF_STOCK("Out of stock"),
    UNAVAILABLE("Unavailable");

    private final String mappedValue;

    Availability(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static Availability fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (Availability availability : Availability.values()) {
            if (availability.mappedValue.equalsIgnoreCase(mappedValue)) {
                return availability;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (Availability availability : Availability.values()) {
            list.add(availability.mappedValue);
        }
        return list;
    }

    public static List<Availability> getAllEnum() {
        List<Availability> list = new ArrayList<>();
        for (Availability availability : Availability.values()) {
            list.add(availability);
        }
        return list;
    }
}

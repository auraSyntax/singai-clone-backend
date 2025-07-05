package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String mappedValue;

    Status(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static Status fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (Status status : Status.values()) {
            if (status.mappedValue.equalsIgnoreCase(mappedValue)) {
                return status;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (Status status : Status.values()) {
            list.add(status.mappedValue);
        }
        return list;
    }

    public static List<Status> getAllEnum() {
        List<Status> list = new ArrayList<>();
        for (Status status : Status.values()) {
            list.add(status);
        }
        return list;
    }
}

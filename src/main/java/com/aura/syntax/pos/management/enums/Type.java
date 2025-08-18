package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum Type {
    INGREDIENT("Ingredient"),
    RETAIL_ITEM("Retail Item");

    private final String mappedValue;

    Type(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static Type fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (Type type : Type.values()) {
            if (type.mappedValue.equalsIgnoreCase(mappedValue)) {
                return type;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (Type type : Type.values()) {
            list.add(type.mappedValue);
        }
        return list;
    }

    public static List<Type> getAllEnum() {
        List<Type> list = new ArrayList<>();
        for (Type type : Type.values()) {
            list.add(type);
        }
        return list;
    }
}

package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum UserType {
    CASHIER("Cashier"),
    WAITER("Waiter"),
    KITCHEN_STAFF("Kitchen Staff");

    private final String mappedValue;

    UserType(String mappedValue) {
        this.mappedValue = mappedValue;
    }
    public static UserType fromMappedValue(String mappedValue){
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (UserType userType : UserType.values()) {
            if (userType.mappedValue.equalsIgnoreCase(mappedValue)) {
                return userType;
            }
        }
        throw new ServiceException("Unsupported type: " + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }
    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (UserType userType : UserType.values()) {
            list.add(userType.mappedValue);
        }
        return list;
    }

}

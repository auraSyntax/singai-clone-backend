package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum PaymentMethod {
    CASH("Cash"),
    CARD("Card"),
    UPI("Upi"),
    OTHER("Other");

    private final String mappedValue;

    PaymentMethod(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static PaymentMethod fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.mappedValue.equalsIgnoreCase(mappedValue)) {
                return paymentMethod;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            list.add(paymentMethod.mappedValue);
        }
        return list;
    }

    public static List<PaymentMethod> getAllEnum() {
        List<PaymentMethod> list = new ArrayList<>();
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            list.add(paymentMethod);
        }
        return list;
    }
}

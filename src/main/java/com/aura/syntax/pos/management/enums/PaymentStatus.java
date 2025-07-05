package com.aura.syntax.pos.management.enums;

import com.aura.syntax.pos.management.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid"),
    PARTIALLY_PAID("Partially paid"),
    REFUNDED("Refunded");

    private final String mappedValue;

    PaymentStatus(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static PaymentStatus fromMappedValue(String mappedValue) {
        if (mappedValue == null || mappedValue.isBlank()) {
            return null;
        }
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.mappedValue.equalsIgnoreCase(mappedValue)) {
                return paymentStatus;
            }
        }
        throw new ServiceException("Unsupported type" + mappedValue, "Bad request", HttpStatus.BAD_REQUEST);
    }


    public String getMappedValue() {
        return mappedValue;
    }

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            list.add(paymentStatus.mappedValue);
        }
        return list;
    }

    public static List<PaymentStatus> getAllEnum() {
        List<PaymentStatus> list = new ArrayList<>();
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            list.add(paymentStatus);
        }
        return list;
    }
}

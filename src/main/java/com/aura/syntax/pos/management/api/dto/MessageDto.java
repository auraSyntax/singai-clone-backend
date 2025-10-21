package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private String subject;
    private String message;
}

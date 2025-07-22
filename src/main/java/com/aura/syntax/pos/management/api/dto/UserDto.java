package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private Long roleId;
    private String roleName;
}

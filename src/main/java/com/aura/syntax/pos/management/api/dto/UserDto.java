package com.aura.syntax.pos.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Long roleId;
    private String roleName;
    private String phoneNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String password;


    public UserDto(Long id, String userName, String email,String phoneNumber,Boolean isActive, Long roleId) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.roleId = roleId;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}

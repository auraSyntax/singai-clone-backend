package com.aura.syntax.pos.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String roleName;
    private boolean isActive;
}

package com.aura.syntax.pos.management.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {
    private String userId;
    private String email;
    private Long roleId;
}

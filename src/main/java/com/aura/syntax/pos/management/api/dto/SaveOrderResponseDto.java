package com.aura.syntax.pos.management.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveOrderResponseDto {
    private Long id;
    private Long waiterId;
    private Long tableId;
    private String orderType;
    private String orderStatus;
    private String orderNumber;
}

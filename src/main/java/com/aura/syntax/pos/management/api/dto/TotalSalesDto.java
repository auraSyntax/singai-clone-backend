package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

@Data
public class TotalSalesDto {
    private Double totalAmount;
    private Double byCard;
    private Double byCash;
    private Double byUpi;
    private Double byOther;
}

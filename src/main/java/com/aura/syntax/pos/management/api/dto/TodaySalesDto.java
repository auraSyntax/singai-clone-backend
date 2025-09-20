package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

@Data
public class TodaySalesDto {
    private Double totalAmount;
    private Double byCard;
    private Double byCash;
    private Double byUpi;
    private Double byOther;
}

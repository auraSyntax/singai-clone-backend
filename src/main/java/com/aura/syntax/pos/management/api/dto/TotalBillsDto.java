package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

@Data
public class TotalBillsDto {
    private Integer totalBills;
    private Integer byCard;
    private Integer byCash;
    private Integer byUpi;
    private Integer byOther;
}

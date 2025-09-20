package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

@Data
public class OrderSummaryDto {
    private TotalBillsDto totalBillsDto;
    private TodayBillsDto todayBillsDto;
    private TotalSalesDto totalSalesDto;
    private TodaySalesDto todaySalesDto;
}

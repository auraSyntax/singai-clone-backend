package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.SalesDto;
import com.aura.syntax.pos.management.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/sales")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://singai-pos.onrender.com", "https://astounding-monstera-9f8e37.netlify.app"})
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public SalesDto getAllSales(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        return salesService.getAllSales(startDate,endDate);
    }
}

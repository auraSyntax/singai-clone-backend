package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.StockDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","https://singai-pos.onrender.com","https://astounding-monstera-9f8e37.netlify.app"})
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseDto addStock(@RequestBody StockDto stockDto){
        return stockService.addStock(stockDto);
    }

    @GetMapping("/list")
    public List<StockDto> getAllIngredients(){
        return stockService.getAllStocks();
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<StockDto> getAllStocksPagination(@RequestParam(value = "page") Integer page,
                                                                      @RequestParam(value = "size") Integer size,
                                                                      @RequestParam(value = "search",required = false) String search){
        return stockService.getAllStocksPagination(page,size,search);
    }

    @GetMapping("/get-by-id")
    public StockDto getStockById(@RequestParam(value = "id") Long id){
        return stockService.getStockById(id);
    }

    @PutMapping
    public ResponseDto updateStock(@RequestBody StockDto ingredientsDto){
        return stockService.updateStock(ingredientsDto);
    }

    @DeleteMapping
    public ResponseDto deleteStock(@RequestParam(value = "id") Long id){
        return stockService.deleteStock(id);
    }
}

package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.StockDto;
import com.aura.syntax.pos.management.api.dto.StockItemsDto;
import com.aura.syntax.pos.management.entity.Stock;
import com.aura.syntax.pos.management.entity.StockItems;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.StockItemRepository;
import com.aura.syntax.pos.management.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private final StockItemRepository stockItemRepository;

    public ResponseDto addStock(StockDto stockDto) {
        Stock stock = Stock.builder()
                .id(stockDto.getId())
                .total(stockDto.getTotal())
                .dateTime(stockDto.getDateTime())
                .invoiceNumber(stockDto.getInvoiceNumber())
                .stockItems(stockDto.getStockItemsDtos() != null && !stockDto.getStockItemsDtos().isEmpty() ?
                        convertStockItems(stockDto.getStockItemsDtos()) : null)
                .createdAt(LocalDateTime.now())
                .isActive(Boolean.TRUE)
                .build();

        stockRepository.save(stock);

        return new ResponseDto("Ingredient saved");
    }

    private Set<StockItems> convertStockItems(Set<StockItemsDto> stockItemsDtos) {
        Set<StockItems> stockItems = new HashSet<>();
        stockItemsDtos.stream().forEach(stockItemsDto -> {
            StockItems stockItem = StockItems.builder()
                    .id(stockItemsDto.getId())
                    .productId(stockItemsDto.getProductId())
                    .salesPrice(stockItemsDto.getSalesPrice())
                    .retailPrice(stockItemsDto.getRetailPrice())
                    .unit(stockItemsDto.getUnit())
                    .quantity(stockItemsDto.getQuantity())
                    .costPerUnit(stockItemsDto.getCostPerUnit())
                    .isActive(Boolean.TRUE)
                    .createdAt(LocalDateTime.now())
                    .build();
            stockItems.add(stockItem);
        });

        return stockItems;
    }

    public List<StockDto> getAllStocks() {
        return stockRepository.getAllStock();
    }

    public PaginatedResponseDto<StockDto> getAllStocksPagination(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<StockDto> ingredientsDtos = stockRepository.getAllStockPagination(pageable,search);
        PaginatedResponseDto<StockDto> ingredientsDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<StockDto> ingredientsDtosContent = ingredientsDtos.getContent();
        ingredientsDtoPaginatedResponseDto.setData(ingredientsDtosContent);
        ingredientsDtoPaginatedResponseDto.setCurrentPage(page);
        ingredientsDtoPaginatedResponseDto.setTotalPages(ingredientsDtos.getTotalPages());
        ingredientsDtoPaginatedResponseDto.setTotalItems(ingredientsDtos.getTotalElements());

        return ingredientsDtoPaginatedResponseDto;
    }

    public StockDto getStockById(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new
                ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
        return StockDto.builder()
                .id(stock.getId())
                .total(stock.getTotal())
                .invoiceNumber(stock.getInvoiceNumber())
                .dateTime(stock.getDateTime())
                .stockItemsDtos(stock.getStockItems() != null && !stock.getStockItems().isEmpty() ?
                        convertStockItemsToDto(stock.getStockItems()) : null)
                .build();
    }

    private Set<StockItemsDto> convertStockItemsToDto(Set<StockItems> stockItems) {
        Set<StockItemsDto> stockItemsDtos = new HashSet<>();
        stockItems.stream().forEach(stockItem -> {
            StockItemsDto itemsDto = StockItemsDto.builder()
                    .id(stockItem.getId())
                    .productId(stockItem.getProductId())
                    .salesPrice(stockItem.getSalesPrice())
                    .retailPrice(stockItem.getRetailPrice())
                    .unit(stockItem.getUnit())
                    .quantity(stockItem.getQuantity())
                    .costPerUnit(stockItem.getCostPerUnit())
                    .isActive(Boolean.TRUE)
                    .build();
            stockItemsDtos.add(itemsDto);
        });

        return stockItemsDtos;
    }

    public ResponseDto deleteStock(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new
                ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
        stockRepository.deleteById(id);
        return new ResponseDto("Stock deleted successfully");
    }

    public ResponseDto updateStock(StockDto stockDto) {
        Stock stock = stockRepository.findById(stockDto.getId()).orElseThrow(() -> new
                ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
        stock.setId(stockDto.getId());
        stock.setTotal(stockDto.getTotal());
        stock.setDateTime(stockDto.getDateTime());
        stock.setInvoiceNumber(stockDto.getInvoiceNumber());
        stock.setUpdatedAt(LocalDateTime.now());
        stock.setStockItems(stockDto.getStockItemsDtos() != null && !stockDto.getStockItemsDtos().isEmpty() ?
                stockDto.getStockItemsDtos().stream().map(stockItemsDto -> {
                    StockItems stockItem = stockItemRepository.findById(stockItemsDto.getId()).orElseThrow(() -> new
                            ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
                    stockItem.setProductId(stockItemsDto.getProductId());
                    stockItem.setUnit(stockItemsDto.getUnit());
                    stockItem.setSalesPrice(stockItemsDto.getSalesPrice());
                    stockItem.setRetailPrice(stockItemsDto.getRetailPrice());
                    stockItem.setQuantity(stockItemsDto.getQuantity());
                    stockItem.setCostPerUnit(stockItemsDto.getCostPerUnit());
                    stockItem.setUpdatedAt(LocalDateTime.now());
                    return stockItem;
                }).collect(Collectors.toSet()) : null);
        stockRepository.save(stock);
        return new ResponseDto("Stock updated successfully");
    }
}

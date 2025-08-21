// package com.aura.syntax.pos.management.service;

// import com.aura.syntax.pos.management.api.dto.StockDto;
// import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
// import com.aura.syntax.pos.management.api.dto.ResponseDto;
// import com.aura.syntax.pos.management.entity.Stock;
// import com.aura.syntax.pos.management.exception.ServiceException;
// import com.aura.syntax.pos.management.repository.StockRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// public class IngredientsService {

//     private final StockRepository stockRepository;

//     public ResponseDto addStock(StockDto stockDto) {
//         Stock stock = Stock.builder()
//                 .id(stockDto.getId())
//                 .stockName(stockDto.getStockName())
//                 .currentStock(stockDto.getCurrentStock())
//                 .minimumStock(stockDto.getMinimumStock())
//                 .unit(stockDto.getUnit())
//                 .costPerUnit(stockDto.getCostPerUnit())
//                 .createdAt(LocalDateTime.now())
//                 .isActive(stockDto.isActive())
//                 .build();

//         stockRepository.save(stock);

//         return new ResponseDto("Ingredient saved");
//     }

//     public List<StockDto> getAllStocks() {
//         return stockRepository.getAllStock();
//     }

//     public PaginatedResponseDto<StockDto> getAllStocksPagination(Integer page, Integer size, String search) {
//         Pageable pageable = PageRequest.of(page - 1,size);
//         Page<StockDto> ingredientsDtos = stockRepository.getAllStockPagination(pageable,search);
//         PaginatedResponseDto<StockDto> ingredientsDtoPaginatedResponseDto = new PaginatedResponseDto<>();
//         List<StockDto> ingredientsDtosContent = ingredientsDtos.getContent();
//         ingredientsDtoPaginatedResponseDto.setData(ingredientsDtosContent);
//         ingredientsDtoPaginatedResponseDto.setCurrentPage(page);
//         ingredientsDtoPaginatedResponseDto.setTotalPages(ingredientsDtos.getTotalPages());
//         ingredientsDtoPaginatedResponseDto.setTotalItems(ingredientsDtos.getTotalElements());

//         return ingredientsDtoPaginatedResponseDto;
//     }

//     public StockDto getStockById(Long id) {
//         Stock stock = stockRepository.findById(id).orElseThrow(() -> new
//                 ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
//         return StockDto.builder()
//                 .id(stock.getId())
//                 .stockName(stock.getStockName())
//                 .currentStock(stock.getCurrentStock())
//                 .minimumStock(stock.getMinimumStock())
//                 .unit(stock.getUnit())
//                 .costPerUnit(stock.getCostPerUnit())
//                 .isActive(stock.isActive())
//                 .build();
//     }

//     public ResponseDto deleteStock(Long id) {
//         Stock stock = stockRepository.findById(id).orElseThrow(() -> new
//                 ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
//         stockRepository.deleteById(id);
//         return new ResponseDto("Stock deleted successfully");
//     }

//     public ResponseDto updateStock(StockDto ingredientsDto) {
//         Stock stock = stockRepository.findById(ingredientsDto.getId()).orElseThrow(() -> new
//                 ServiceException("Stock not found","Bad request", HttpStatus.BAD_REQUEST));
//         stock.setId(ingredientsDto.getId());
//         stock.setStockName(ingredientsDto.getStockName());
//         stock.setCurrentStock(ingredientsDto.getCurrentStock());
//         stock.setMinimumStock(ingredientsDto.getMinimumStock());
//         stock.setUnit(ingredientsDto.getUnit());
//         stock.setCostPerUnit(ingredientsDto.getCostPerUnit());
//         stock.setUpdatedAt(LocalDateTime.now());
//         stockRepository.save(stock);
//         return new ResponseDto("Stock updated successfully");
//     }
// }

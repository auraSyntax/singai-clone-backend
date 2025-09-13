package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.*;
import com.aura.syntax.pos.management.entity.MenuItems;
import com.aura.syntax.pos.management.entity.Product;
import com.aura.syntax.pos.management.entity.Stock;
import com.aura.syntax.pos.management.entity.StockItems;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.enums.Type;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.MenuItemsRepository;
import com.aura.syntax.pos.management.repository.ProductRepository;
import com.aura.syntax.pos.management.repository.StockItemRepository;
import com.aura.syntax.pos.management.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private final StockItemRepository stockItemRepository;

    private final ProductRepository productRepository;

    private final MenuItemsRepository menuItemsRepository;

    public ResponseDto addStock(StockDto stockDto) {
        Stock stock = Stock.builder()
                .id(stockDto.getId())
                .total(stockDto.getTotal())
                .dateTime(stockDto.getDateTime())
                .invoiceNumber(stockDto.getInvoiceNumber())
                .stockItems(stockDto.getStockItemsDtos() != null && !stockDto.getStockItemsDtos().isEmpty() ?
                        convertStockItems(stockDto.getStockItemsDtos(), stockDto.getProductDtos()) : null)
                .createdAt(LocalDateTime.now())
                .isActive(Boolean.TRUE)
                .stockName(stockDto.getStockName())
                .build();

        stockRepository.save(stock);

        return new ResponseDto("Ingredient saved");
    }

    private Set<StockItems> convertStockItems(Set<StockItemsDto> stockItemsDtos, Set<ProductDto> productDtos) {
        Set<StockItems> stockItems = new HashSet<>();

        Map<Long, Long> newProductIdMap = new HashMap<>();
        if (productDtos != null && !productDtos.isEmpty()) {
            for (ProductDto productDto : productDtos) {
                Product product = Product.builder()
                        .id(productDto.getId())
                        .productName(productDto.getProductName())
                        .minimumStock(productDto.getMinimumStock())
                        .currentStock(productDto.getCurrentStock())
                        .type(Type.fromMappedValue(productDto.getType()))
                        .createdAt(LocalDateTime.now())
                        .isActive(Boolean.TRUE)
                        .build();
                productRepository.save(product);
                newProductIdMap.put(productDto.getId(), product.getId());

                if (productDto.getType().equalsIgnoreCase(Type.RETAIL_ITEM.getMappedValue())) {
                    MenuItems menuItems = MenuItems.builder()
                            .name(productDto.getProductName())
                            .categoryId(productDto.getCategoryId())
                            .description(productDto.getDescription())
                            .imageUrl(productDto.getImageUrl())
                            .price(productDto.getPrice())
                            .createdAt(LocalDateTime.now())
                            .status(Status.ACTIVE)
                            .build();
                    menuItemsRepository.save(menuItems);
                }

                StockItems stockItem = StockItems.builder()
                        .productId(product.getId())
                        .salesPrice(productDto.getSalesPrice())
                        .retailPrice(productDto.getRetailPrice())
                        .unit(productDto.getUnit())
                        .quantity(productDto.getQuantity())
                        .costPerUnit(productDto.getCostPerUnit())
                        .isActive(Boolean.TRUE)
                        .createdAt(LocalDateTime.now())
                        .build();

                stockItems.add(stockItem);

            }
        }

        for (StockItemsDto stockItemsDto : stockItemsDtos) {
            Long productId = stockItemsDto.getProductId();

            if (productId == null && !newProductIdMap.isEmpty()) {
                productId = newProductIdMap.values().iterator().next();
            } else {
                Product existingProduct = productRepository.findById(productId)
                        .orElseThrow(() -> new ServiceException("Product not found", "Bad request", HttpStatus.BAD_REQUEST));
                existingProduct.setCurrentStock(existingProduct.getCurrentStock() + stockItemsDto.getQuantity());
                productRepository.save(existingProduct);
            }

            StockItems stockItem = StockItems.builder()
                    .id(stockItemsDto.getId())
                    .productId(productId)
                    .salesPrice(stockItemsDto.getSalesPrice())
                    .retailPrice(stockItemsDto.getRetailPrice())
                    .unit(stockItemsDto.getUnit())
                    .quantity(stockItemsDto.getQuantity())
                    .costPerUnit(stockItemsDto.getCostPerUnit())
                    .isActive(Boolean.TRUE)
                    .createdAt(LocalDateTime.now())
                    .build();

            stockItems.add(stockItem);
        }

        return stockItems;
    }


    public List<StockDto> getAllStocks() {
        return stockRepository.getAllStock();
    }

    public PaginatedResponseDto<StockDto> getAllStocksPagination(Integer page, Integer size, String search,Long productId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<StockDto> ingredientsDtos = stockRepository.getAllStockPagination(pageable, search);
        PaginatedResponseDto<StockDto> ingredientsDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<StockDto> ingredientsDtosContent = ingredientsDtos.getContent();
        ingredientsDtosContent.forEach(stockDto -> {
            Set<StockItemsDto> stockItemsDtos = stockRepository.getAllStockItemsByStockId(stockDto.getId(),productId);
            stockDto.setStockItemsDtos(stockItemsDtos);
        });
        ingredientsDtoPaginatedResponseDto.setData(ingredientsDtosContent);
        ingredientsDtoPaginatedResponseDto.setCurrentPage(page);
        ingredientsDtoPaginatedResponseDto.setTotalPages(ingredientsDtos.getTotalPages());
        ingredientsDtoPaginatedResponseDto.setTotalItems(ingredientsDtos.getTotalElements());

        return ingredientsDtoPaginatedResponseDto;
    }

    public StockDto getStockById(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new
                ServiceException("Stock not found", "Bad request", HttpStatus.BAD_REQUEST));
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
                    .productName(productRepository.findProductNameById(stockItem.getProductId()))
                    .build();
            stockItemsDtos.add(itemsDto);
        });

        return stockItemsDtos;
    }

    public ResponseDto deleteStock(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new
                ServiceException("Stock not found", "Bad request", HttpStatus.BAD_REQUEST));
        stockRepository.deleteById(id);
        return new ResponseDto("Stock deleted successfully");
    }

    public ResponseDto updateStock(StockDto stockDto) {
        Stock stock = stockRepository.findById(stockDto.getId()).orElseThrow(() -> new
                ServiceException("Stock not found", "Bad request", HttpStatus.BAD_REQUEST));
        stock.setId(stockDto.getId());
        stock.setTotal(stockDto.getTotal());
        stock.setDateTime(stockDto.getDateTime());
        stock.setInvoiceNumber(stockDto.getInvoiceNumber());
        stock.setUpdatedAt(LocalDateTime.now());
        stock.setStockItems(stockDto.getStockItemsDtos() != null && !stockDto.getStockItemsDtos().isEmpty() ?
                stockDto.getStockItemsDtos().stream().map(stockItemsDto -> {
                    StockItems stockItem = stockItemRepository.findById(stockItemsDto.getId()).orElseThrow(() -> new
                            ServiceException("Stock not found", "Bad request", HttpStatus.BAD_REQUEST));
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

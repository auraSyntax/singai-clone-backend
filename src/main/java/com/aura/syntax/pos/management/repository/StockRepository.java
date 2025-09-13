package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.StockDto;
import com.aura.syntax.pos.management.api.dto.StockItemsDto;
import com.aura.syntax.pos.management.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.StockDto(i.id,i.isActive) " +
           "FROM Stock i ")
    List<StockDto> getAllStock();

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.StockDto(i.id, i.stockName, i.invoiceNumber, i.total) " +
           "FROM Stock i " +
           "WHERE (:search IS NULL OR LOWER(i.stockName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<StockDto> getAllStockPagination(Pageable pageable, String search);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.StockItemsDto(s.id, s.quantity, s.costPerUnit, s.unit, s.salesPrice, s.retailPrice, p.productName) " +
           "FROM StockItems s " +
           "JOIN Product p ON s.productId = p.id " +
           "WHERE s.stockId = :stockId " +
           "AND (:productId IS NULL OR s.productId = :productId) ")
    Set<StockItemsDto> getAllStockItemsByStockId(Long stockId, Long productId);

}

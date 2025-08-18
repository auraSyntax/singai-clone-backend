package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.StockDto;
import com.aura.syntax.pos.management.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.StockDto(i.id,i.isActive) " +
           "FROM Stock i ")
    List<StockDto> getAllStock();

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.StockDto(i.id,i.isActive) " +
           "FROM Stock i ")
    Page<StockDto> getAllStockPagination(Pageable pageable, String search);
}

package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.entity.StockItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItems,Long> {
}

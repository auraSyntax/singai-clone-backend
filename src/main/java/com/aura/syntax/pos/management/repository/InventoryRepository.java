package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,String> {
}

package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.entity.MenuItemIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemStockRepository extends JpaRepository<MenuItemIngredients,Long> {
}

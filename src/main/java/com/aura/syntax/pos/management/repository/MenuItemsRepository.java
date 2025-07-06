package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.entity.MenuItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemsRepository extends JpaRepository<MenuItems,Long> {
}

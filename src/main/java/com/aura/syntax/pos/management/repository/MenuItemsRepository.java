package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.MenuItemsDto;
import com.aura.syntax.pos.management.entity.MenuItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemsRepository extends JpaRepository<MenuItems,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.MenuItemsDto(m.id,m.name,m.description,m.price,m.imageUrl,m.preparationTime,m.status,m.categoryId) " +
           "FROM MenuItems m " +
           "WHERE :categoryId IS NULL OR m.categoryId = :categoryId " +
           "AND m.status = 'ACTIVE' " +
           "AND :search IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "ORDER BY m.createdAt")
    List<MenuItemsDto> getAllMenuItems(Long categoryId,String search);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.MenuItemsDto(m.id,m.name,m.description,m.price,m.imageUrl,m.preparationTime,m.status,m.categoryId) " +
           "FROM MenuItems m " +
           "WHERE :search IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "AND :categoryId IS NULL OR m.categoryId = :categoryId " +
           "ORDER BY m.createdAt")
    Page<MenuItemsDto> getAllMenuItemsPagination(Pageable pageable, String search, Long categoryId);

    @Query("SELECT m.name FROM MenuItems m " +
           "WHERE m.id = :menuItemsId")
    String getMenuItemById(Long menuItemsId);
}

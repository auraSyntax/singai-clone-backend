package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.ProductDto;
import com.aura.syntax.pos.management.api.dto.StockItemsDto;
import com.aura.syntax.pos.management.entity.Product;
import com.aura.syntax.pos.management.entity.StockItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.ProductDto(p.id,p.productName,p.currentStock,p.minimumStock,p.type,p.isActive) " +
           "FROM Product p " +
           "WHERE :search IS NULL OR p.productName LIKE %:search%")
    Page<ProductDto> getAllProductItems(Pageable pageable, String search);

    @Query("SELECT p.productName " +
           "FROM Product p " +
           "WHERE p.id = :id")
    String findProductNameById(Long id);
}

package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.CategoryDto;
import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Categories, Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.CategoryDto(c.id,c.name,c.description,c.imageUrl,c.status,c.mainCategoryId) " +
           "FROM Categories c " +
           "WHERE c.mainCategoryId = :mainCategoryId " +
           "AND c.status = 'ACTIVE' " +
           "ORDER BY c.createdAt")
    List<CategoryDto> getAllCategories(Long mainCategoryId);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.CategoryDto(c.id,c.name,c.description,c.imageUrl,c.status,c.mainCategoryId) " +
           "FROM Categories c " +
           "WHERE :search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "AND :mainCategoryId IS NULL OR c.mainCategoryId = :mainCategoryId " +
           "ORDER BY c.createdAt")
    Page<CategoryDto> getAllCategoriesPagination(Pageable pageable, String search, Long mainCategoryId);
}

package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.entity.MainCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MainCategoriesRepository extends JpaRepository<MainCategories,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.MainCategoryDto(m.id,m.mainCategoryName) " +
           "FROM MainCategories m " +
           "WHERE m.status = 'ACTIVE'")
    List<MainCategoryDto> getAllMainCategories();

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.MainCategoryDto(m.id, m.mainCategoryName,m.description,m.imageUrl,m.status) " +
           "FROM MainCategories m " +
           "WHERE :search IS NULL OR LOWER(m.mainCategoryName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MainCategoryDto> getAllMainCategoriesPagination(Pageable pageable, String search);

}

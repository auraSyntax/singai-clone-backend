package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.IngredientsDto;
import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.entity.Ingredients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientsRepository extends JpaRepository<Ingredients,Long> {

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.IngredientsDto(i.id,i.name,i.unit,i.currentStock,i.minimumStock,i.costPerUnit) " +
           "FROM Ingredients i ")
    List<IngredientsDto> getAllIngredients();

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.IngredientsDto(i.id,i.name,i.unit,i.currentStock,i.minimumStock,i.costPerUnit) " +
           "FROM Ingredients i " +
           "WHERE :search IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<IngredientsDto> getAllIngredientsPagination(Pageable pageable, String search);
}

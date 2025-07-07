package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.IngredientsDto;
import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.entity.Ingredients;
import com.aura.syntax.pos.management.entity.MainCategories;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.IngredientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientsService {

    private final IngredientsRepository ingredientsRepository;

    public ResponseDto addIngredient(IngredientsDto ingredientsDto) {
        Ingredients ingredients = Ingredients.builder()
                .id(ingredientsDto.getId())
                .name(ingredientsDto.getName())
                .currentStock(ingredientsDto.getCurrentStock())
                .minimumStock(ingredientsDto.getMinimumStock())
                .unit(ingredientsDto.getUnit())
                .costPerUnit(ingredientsDto.getCostPerUnit())
                .createdAt(LocalDateTime.now())
                .build();

        ingredientsRepository.save(ingredients);

        return new ResponseDto("Ingredient saved");
    }

    public List<IngredientsDto> getAllIngredients() {
        return ingredientsRepository.getAllIngredients();
    }

    public PaginatedResponseDto<IngredientsDto> getAllIngredientsPagination(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<IngredientsDto> ingredientsDtos = ingredientsRepository.getAllIngredientsPagination(pageable,search);
        PaginatedResponseDto<IngredientsDto> ingredientsDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<IngredientsDto> ingredientsDtosContent = ingredientsDtos.getContent();
        ingredientsDtoPaginatedResponseDto.setData(ingredientsDtosContent);
        ingredientsDtoPaginatedResponseDto.setCurrentPage(page);
        ingredientsDtoPaginatedResponseDto.setTotalPages(ingredientsDtos.getTotalPages());
        ingredientsDtoPaginatedResponseDto.setTotalItems(ingredientsDtos.getTotalElements());

        return ingredientsDtoPaginatedResponseDto;
    }

    public IngredientsDto getIngredientById(Long id) {
        Ingredients ingredients = ingredientsRepository.findById(id).orElseThrow(() -> new
                ServiceException("Ingredient not found","Bad request", HttpStatus.BAD_REQUEST));
        return IngredientsDto.builder()
                .id(ingredients.getId())
                .name(ingredients.getName())
                .currentStock(ingredients.getCurrentStock())
                .minimumStock(ingredients.getMinimumStock())
                .unit(ingredients.getUnit())
                .costPerUnit(ingredients.getCostPerUnit())
                .build();
    }

    public ResponseDto deleteIngredient(Long id) {
        Ingredients ingredients = ingredientsRepository.findById(id).orElseThrow(() -> new
                ServiceException("Ingredient not found","Bad request", HttpStatus.BAD_REQUEST));
        ingredientsRepository.deleteById(id);
        return new ResponseDto("Ingredient deleted successfully");
    }

    public ResponseDto updateIngredient(IngredientsDto ingredientsDto) {
        Ingredients ingredients = ingredientsRepository.findById(ingredientsDto.getId()).orElseThrow(() -> new
                ServiceException("Ingredient not found","Bad request", HttpStatus.BAD_REQUEST));
        ingredients.setId(ingredientsDto.getId());
        ingredients.setName(ingredientsDto.getName());
        ingredients.setCurrentStock(ingredientsDto.getCurrentStock());
        ingredients.setMinimumStock(ingredientsDto.getMinimumStock());
        ingredients.setUnit(ingredientsDto.getUnit());
        ingredients.setCostPerUnit(ingredientsDto.getCostPerUnit());
        ingredients.setUpdatedAt(LocalDateTime.now());
        ingredientsRepository.save(ingredients);
        return new ResponseDto("Ingredient updated successfully");
    }
}

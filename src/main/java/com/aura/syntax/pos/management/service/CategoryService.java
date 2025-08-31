package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.CategoryDto;
import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.entity.Categories;
import com.aura.syntax.pos.management.entity.MainCategories;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.CategoryRepository;
import com.aura.syntax.pos.management.repository.MainCategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final MainCategoriesRepository mainCategoriesRepository;

    @Value("${cloudinary.base.url}")
    private String imagePath;

    public ResponseDto addCategory(CategoryDto categoryDto){
        MainCategories existingMainCategory = mainCategoriesRepository.findById(categoryDto.getMainCategoryId()).orElseThrow(() -> new
                ServiceException("Main category not found","Bad request", HttpStatus.BAD_REQUEST));
        Categories categories = Categories.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getCategoryName())
                .description(categoryDto.getDescription())
                .imageUrl(categoryDto.getImageUrl())
                .status(Status.ACTIVE)
                .mainCategoryId(categoryDto.getMainCategoryId())
                .createdAt(LocalDateTime.now())
                .build();

        categoryRepository.save(categories);

        return new ResponseDto("Category saved successfully");

    }

    public List<CategoryDto> getAllCategories(Long mainCategoryId) {
        List<CategoryDto> categoryDtos = categoryRepository.getAllCategories(mainCategoryId);
        categoryDtos.stream().forEach(categoryDto -> {
            categoryDto.setMainCategoryName(mainCategoriesRepository.getMainCategoryNameById(categoryDto.getMainCategoryId()));
            categoryDto.setImageUrl(categoryDto.getImageUrl() != null ? imagePath + categoryDto.getImageUrl() : null);
        });

        return categoryDtos;
    }

    public PaginatedResponseDto<CategoryDto> getAllCategoriesPagination(Integer page, Integer size, String search,Long mainCategoryId) {
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<CategoryDto> categoryDtos = categoryRepository.getAllCategoriesPagination(pageable,search,mainCategoryId);
        PaginatedResponseDto<CategoryDto> categoryDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<CategoryDto> categories = categoryDtos.getContent();
        categoryDtos.stream().forEach(categoryDto -> {
            categoryDto.setMainCategoryName(mainCategoriesRepository.getMainCategoryNameById(categoryDto.getMainCategoryId()));
            categoryDto.setImageUrl(categoryDto.getImageUrl() != null ? imagePath + categoryDto.getImageUrl() : null);
        });
        categoryDtoPaginatedResponseDto.setData(categories);
        categoryDtoPaginatedResponseDto.setCurrentPage(page);
        categoryDtoPaginatedResponseDto.setTotalPages(categoryDtos.getTotalPages());
        categoryDtoPaginatedResponseDto.setTotalItems(categoryDtos.getTotalElements());

        return categoryDtoPaginatedResponseDto;
    }

    public CategoryDto getCategoryById(Long id) {

        Categories categories = categoryRepository.findById(id).orElseThrow(() -> new
                ServiceException("Category not found","Bad request", HttpStatus.BAD_REQUEST));

        CategoryDto mainCategoryDto = CategoryDto.builder()
                .id(categories.getId())
                .categoryName(categories.getName())
                .imageUrl(categories.getImageUrl())
                .description(categories.getDescription())
                .mainCategoryId(categories.getMainCategoryId())
                .mainCategoryName(mainCategoriesRepository.getMainCategoryNameById(categories.getMainCategoryId()))
                .build();
        return mainCategoryDto;
    }

    public ResponseDto updateCategory(CategoryDto categoryDto) {
        Categories existingCategory = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new
                ServiceException("Category not found","Bad request", HttpStatus.BAD_REQUEST));
        MainCategories existingMainCategory = mainCategoriesRepository.findById(categoryDto.getMainCategoryId()).orElseThrow(() -> new
                ServiceException("Main category not found","Bad request", HttpStatus.BAD_REQUEST));

        existingCategory.setId(categoryDto.getId());
        existingCategory.setName(categoryDto.getCategoryName());
        existingCategory.setDescription(categoryDto.getDescription());
        existingCategory.setImageUrl(categoryDto.getImageUrl());
        existingCategory.setUpdatedAt(LocalDateTime.now());
        existingCategory.setMainCategoryId(categoryDto.getMainCategoryId());
        categoryRepository.save(existingCategory);

        return new ResponseDto("Category updated successfully");

    }

    public ResponseDto updateStatus(Long id, String status) {
        Categories existingCategory = categoryRepository.findById(id).orElseThrow(() -> new
                ServiceException("Category not found","Bad request", HttpStatus.BAD_REQUEST));

        existingCategory.setStatus(Status.fromMappedValue(status));
        categoryRepository.save(existingCategory);
        return new ResponseDto("Category status updated successfully");

    }

    public ResponseDto deleteMainCategory(Long id) {
        Categories existingCategory = categoryRepository.findById(id).orElseThrow(() -> new
                ServiceException("Category not found","Bad request", HttpStatus.BAD_REQUEST));
        categoryRepository.deleteById(id);
        return new ResponseDto("Category deleted successfully");

    }
}

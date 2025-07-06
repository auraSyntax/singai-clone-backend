package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.entity.MainCategories;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.MainCategoriesRepository;
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
public class MainCategoryService {

    private final MainCategoriesRepository mainCategoriesRepository;

    public ResponseDto addMainCategory(MainCategoryDto mainCategoryDto){
        MainCategories mainCategories = MainCategories.builder()
                .id(mainCategoryDto.getId())
                .mainCategoryName(mainCategoryDto.getMainCategoryName())
                .description(mainCategoryDto.getDescription())
                .imageUrl(mainCategoryDto.getImageUrl())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        mainCategoriesRepository.save(mainCategories);

        return new ResponseDto("Main category saved successfully");

    }

    public List<MainCategoryDto> getAllMainCategories() {
        return mainCategoriesRepository.getAllMainCategories();
    }

    public PaginatedResponseDto<MainCategoryDto> getAllMainCategoriesPagination(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<MainCategoryDto> mainCategoryDtos = mainCategoriesRepository.getAllMainCategoriesPagination(pageable,search);
        PaginatedResponseDto<MainCategoryDto> mainCategoryDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<MainCategoryDto> mainCategories = mainCategoryDtos.getContent();
        mainCategoryDtoPaginatedResponseDto.setData(mainCategories);
        mainCategoryDtoPaginatedResponseDto.setCurrentPage(page);
        mainCategoryDtoPaginatedResponseDto.setTotalPages(mainCategoryDtos.getTotalPages());
        mainCategoryDtoPaginatedResponseDto.setTotalItems(mainCategoryDtos.getTotalElements());

        return mainCategoryDtoPaginatedResponseDto;
    }

    public MainCategoryDto getMainCategoryById(Long id) {

        MainCategories mainCategories = mainCategoriesRepository.findById(id).orElseThrow(() -> new
                ServiceException("Main category not found","Bad request", HttpStatus.BAD_REQUEST));

        MainCategoryDto mainCategoryDto = MainCategoryDto.builder()
                .id(mainCategories.getId())
                .mainCategoryName(mainCategories.getMainCategoryName())
                .imageUrl(mainCategories.getImageUrl())
                .description(mainCategories.getDescription())
                .build();
        return mainCategoryDto;
    }

    public ResponseDto updateMainCategory(MainCategoryDto mainCategoryDto) {
        MainCategories existingMainCategory = mainCategoriesRepository.findById(mainCategoryDto.getId()).orElseThrow(() -> new
                ServiceException("Main category not found","Bad request", HttpStatus.BAD_REQUEST));

        existingMainCategory.setId(mainCategoryDto.getId());
        existingMainCategory.setMainCategoryName(mainCategoryDto.getMainCategoryName());
        existingMainCategory.setDescription(mainCategoryDto.getDescription());
        existingMainCategory.setImageUrl(mainCategoryDto.getImageUrl());
        existingMainCategory.setUpdatedAt(LocalDateTime.now());
        mainCategoriesRepository.save(existingMainCategory);

        return new ResponseDto("Main category updated successfully");

    }

    public ResponseDto updateStatus(Long id, String status) {
        MainCategories existingMainCategory = mainCategoriesRepository.findById(id).orElseThrow(() -> new
                ServiceException("Main category not found","Bad request", HttpStatus.BAD_REQUEST));

        existingMainCategory.setStatus(Status.fromMappedValue(status));
        mainCategoriesRepository.save(existingMainCategory);
        return new ResponseDto("Main category status updated successfully");

    }

    public ResponseDto deleteMainCategory(Long id) {
        MainCategories existingMainCategory = mainCategoriesRepository.findById(id).orElseThrow(() -> new
                ServiceException("Main category not found","Bad request", HttpStatus.BAD_REQUEST));
        mainCategoriesRepository.deleteById(id);
        return new ResponseDto("Main category deleted successfully");

    }
}

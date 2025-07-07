package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.*;
import com.aura.syntax.pos.management.entity.Categories;
import com.aura.syntax.pos.management.entity.MenuItemIncredients;
import com.aura.syntax.pos.management.entity.MenuItems;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.CategoryRepository;
import com.aura.syntax.pos.management.repository.MenuItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemsService {

    private final MenuItemsRepository menuItemsRepository;

    private final CategoryRepository categoryRepository;

    public ResponseDto addMenuItem(MenuItemsDto menuItemsDto) {
        Categories categories = categoryRepository.findById(menuItemsDto.getCategoryId()).orElseThrow(() -> new
                ServiceException("Category not found","Bad request", HttpStatus.BAD_REQUEST));

        MenuItems menuItems = MenuItems.builder()
                .id(menuItemsDto.getId())
                .name(menuItemsDto.getName())
                .status(Status.ACTIVE)
                .categoryId(menuItemsDto.getCategoryId())
                .createdAt(LocalDateTime.now())
                .imageUrl(menuItemsDto.getImageUrl())
                .description(menuItemsDto.getDescription())
                .preparationTime(menuItemsDto.getPreparationTime())
                .price(menuItemsDto.getPrice())
                .menuItemIncredients(menuItemsDto.getMenuItemIncredientsDtos() != null && !menuItemsDto.getMenuItemIncredientsDtos().isEmpty() ?
                        menuItemsDto.getMenuItemIncredientsDtos().stream()
                                .map(this::saveMenuItemIncredients)
                                .collect(Collectors.toSet()) : null)
                .build();

        menuItemsRepository.save(menuItems);

        return new ResponseDto("Menu Item Saved");
    }

    private MenuItemIncredients saveMenuItemIncredients(MenuItemIncredientsDto menuItemIncredientsDto) {
        return MenuItemIncredients.builder()
                .id(menuItemIncredientsDto.getId())
                .menuItemsId(menuItemIncredientsDto.getMenuItemsId())
                .createdAt(LocalDateTime.now())
                .ingredientsId(menuItemIncredientsDto.getIngredientsId())
                .quantityRequired(menuItemIncredientsDto.getQuantityRequired())
                .unit(menuItemIncredientsDto.getUnit())
                .build();

    }

    public List<MenuItemsDto> getAllMenuItems(Long categoryId) {
        return menuItemsRepository.getAllMenuItems(categoryId);
    }

    public PaginatedResponseDto<MenuItemsDto> getAllMenuItemsPagination(Integer page, Integer size, String search, Long categoryId) {
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<MenuItemsDto> menuItemsDtos = menuItemsRepository.getAllMenuItemsPagination(pageable,search,categoryId);
        PaginatedResponseDto<MenuItemsDto> menuItemsDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<MenuItemsDto> menuItemsDtoList = menuItemsDtos.getContent();
        menuItemsDtoPaginatedResponseDto.setData(menuItemsDtoList);
        menuItemsDtoPaginatedResponseDto.setCurrentPage(page);
        menuItemsDtoPaginatedResponseDto.setTotalPages(menuItemsDtos.getTotalPages());
        menuItemsDtoPaginatedResponseDto.setTotalItems(menuItemsDtos.getTotalElements());

        return menuItemsDtoPaginatedResponseDto;
    }
}

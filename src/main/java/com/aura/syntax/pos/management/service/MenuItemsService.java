package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.MenuItemIncredientsDto;
import com.aura.syntax.pos.management.api.dto.MenuItemsDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.entity.Categories;
import com.aura.syntax.pos.management.entity.MenuItemIngredients;
import com.aura.syntax.pos.management.entity.MenuItems;
import com.aura.syntax.pos.management.enums.Status;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.CategoryRepository;
import com.aura.syntax.pos.management.repository.MenuItemStockRepository;
import com.aura.syntax.pos.management.repository.MenuItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final MenuItemStockRepository menuItemStockRepository;

    @Value("${cloudinary.base.url}")
    private String imagePath;

    public ResponseDto addMenuItem(MenuItemsDto menuItemsDto) {
        Categories categories = categoryRepository.findById(menuItemsDto.getCategoryId()).orElseThrow(() -> new
                ServiceException("Category not found", "Bad request", HttpStatus.BAD_REQUEST));

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
                .menuItemIngredients(menuItemsDto.getMenuItemIncredientsDtos() != null && !menuItemsDto.getMenuItemIncredientsDtos().isEmpty() ?
                        menuItemsDto.getMenuItemIncredientsDtos().stream()
                                .map(this::saveMenuItemIncredients)
                                .collect(Collectors.toSet()) : null)

                .build();

        menuItemsRepository.save(menuItems);

        return new ResponseDto("Menu Item Saved");
    }

    private MenuItemIngredients saveMenuItemIncredients(MenuItemIncredientsDto menuItemIncredientsDto) {
        return MenuItemIngredients.builder()
                .id(menuItemIncredientsDto.getId())
                .menuItemsId(menuItemIncredientsDto.getMenuItemsId())
                .createdAt(LocalDateTime.now())
                .productId(menuItemIncredientsDto.getProductId())
                .quantityRequired(menuItemIncredientsDto.getQuantityRequired())
                .unit(menuItemIncredientsDto.getUnit())
                .build();

    }

    public List<MenuItemsDto> getAllMenuItems(Long categoryId, String search) {
        return menuItemsRepository.getAllMenuItems(categoryId, search, imagePath);
    }

    public PaginatedResponseDto<MenuItemsDto> getAllMenuItemsPagination(Integer page, Integer size, String search, Long categoryId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<MenuItemsDto> menuItemsDtos = menuItemsRepository.getAllMenuItemsPagination(pageable, search, categoryId,imagePath);
        PaginatedResponseDto<MenuItemsDto> menuItemsDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<MenuItemsDto> menuItemsDtoList = menuItemsDtos.getContent();
//        menuItemsDtoList.stream().forEach(menuItemsDto -> {
//            menuItemsDto.setCategoryName(categoryRepository.getCategoryNameById(menuItemsDto.getCategoryId()));
//            menuItemsDto.setImageUrl(menuItemsDto.getImageUrl() != null ? imagePath + menuItemsDto.getImageUrl() : null);
//        });
        menuItemsDtoPaginatedResponseDto.setData(menuItemsDtoList);
        menuItemsDtoPaginatedResponseDto.setCurrentPage(page);
        menuItemsDtoPaginatedResponseDto.setTotalPages(menuItemsDtos.getTotalPages());
        menuItemsDtoPaginatedResponseDto.setTotalItems(menuItemsDtos.getTotalElements());

        return menuItemsDtoPaginatedResponseDto;
    }

    public MenuItemsDto getMenuItemById(Long id) {
        MenuItems menuItems = menuItemsRepository.findById(id).orElseThrow(() -> new
                ServiceException("Menu Item not found", "Bad request", HttpStatus.BAD_REQUEST));
        return MenuItemsDto.builder()
                .id(menuItems.getId())
                .name(menuItems.getName())
                .description(menuItems.getDescription())
                .price(menuItems.getPrice())
                .categoryId(menuItems.getCategoryId())
                .imageUrlWithDomain(menuItems.getImageUrl() != null ? imagePath + menuItems.getImageUrl() : null)
                .imageUrl(menuItems.getImageUrl())
                .preparationTime(menuItems.getPreparationTime())
                .categoryName(categoryRepository.getCategoryNameById(menuItems.getCategoryId()))
                .build();
    }

    public ResponseDto updateMenuItem(MenuItemsDto menuItemsDto) {
        MenuItems existingMenuItem = menuItemsRepository.findById(menuItemsDto.getId()).orElseThrow(() -> new
                ServiceException("Menu Item not found", "Bad request", HttpStatus.BAD_REQUEST));
        existingMenuItem.setId(menuItemsDto.getId());
        existingMenuItem.setName(menuItemsDto.getName());
        existingMenuItem.setDescription(menuItemsDto.getDescription());
        existingMenuItem.setPrice(menuItemsDto.getPrice());
        existingMenuItem.setCategoryId(menuItemsDto.getCategoryId());
        existingMenuItem.setImageUrl(menuItemsDto.getImageUrl());
        existingMenuItem.setPreparationTime(menuItemsDto.getPreparationTime());
        existingMenuItem.setMenuItemIngredients(menuItemsDto.getMenuItemIncredientsDtos() != null && !menuItemsDto.getMenuItemIncredientsDtos().isEmpty() ?
                menuItemsDto.getMenuItemIncredientsDtos().stream()
                        .map(menuItemIncredientsDto -> {
                            MenuItemIngredients existingMenuItemStock = menuItemStockRepository.findById(menuItemIncredientsDto.getId())
                                    .orElseThrow(() -> new ServiceException("Menu item stock not found", "Bad request", HttpStatus.BAD_REQUEST));
                            updateMenuItemIncredients(menuItemIncredientsDto, existingMenuItemStock);

                            return existingMenuItemStock;
                        })
                        .collect(Collectors.toSet()) : null);
        menuItemsRepository.save(existingMenuItem);

        return new ResponseDto("Menu Item updated successfully");
    }

    private MenuItemIngredients updateMenuItemIncredients(MenuItemIncredientsDto menuItemIncredientsDto, MenuItemIngredients menuItemStock) {
        menuItemStock.setId(menuItemIncredientsDto.getId());
        menuItemStock.setMenuItemsId(menuItemIncredientsDto.getMenuItemsId());
        menuItemStock.setUnit(menuItemIncredientsDto.getUnit());
        menuItemStock.setProductId(menuItemIncredientsDto.getProductId());
        menuItemStock.setQuantityRequired(menuItemIncredientsDto.getQuantityRequired());
        return menuItemStock;
    }

    public ResponseDto updateStatus(Long id, String status) {
        MenuItems existingMenuItems = menuItemsRepository.findById(id).orElseThrow(() -> new
                ServiceException("Menu Item not found", "Bad request", HttpStatus.BAD_REQUEST));

        existingMenuItems.setStatus(Status.fromMappedValue(status));
        menuItemsRepository.save(existingMenuItems);
        return new ResponseDto("Menu Item status updated successfully");
    }

    public ResponseDto deleteMenuItem(Long id) {
        MenuItems existingMenuItems = menuItemsRepository.findById(id).orElseThrow(() -> new
                ServiceException("Menu Item not found", "Bad request", HttpStatus.BAD_REQUEST));
        menuItemsRepository.deleteById(id);
        return new ResponseDto("Menu Item deleted successfully");
    }
}

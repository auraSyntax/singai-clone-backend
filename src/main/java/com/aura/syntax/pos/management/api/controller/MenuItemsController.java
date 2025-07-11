package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.CategoryDto;
import com.aura.syntax.pos.management.api.dto.MenuItemsDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.service.MenuItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/menu-items")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class MenuItemsController {

    private final MenuItemsService menuItemsService;

    @PostMapping
    public ResponseDto addMenuItem(@RequestBody MenuItemsDto menuItemsDto){
        return menuItemsService.addMenuItem(menuItemsDto);
    }

    @GetMapping("/list")
    public List<MenuItemsDto> getAllMenuItems(@RequestParam(value = "categoryId",required = false) Long categoryId){
        return menuItemsService.getAllMenuItems(categoryId);
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<MenuItemsDto> getAllMenuItemsPagination(@RequestParam(value = "page") Integer page,
                                                                        @RequestParam(value = "size") Integer size,
                                                                        @RequestParam(value = "search",required = false) String search,
                                                                        @RequestParam(value = "categoryId",required = false) Long categoryId){
        return menuItemsService.getAllMenuItemsPagination(page,size,search,categoryId);
    }
}

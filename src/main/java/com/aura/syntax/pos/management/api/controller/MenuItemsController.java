package com.aura.syntax.pos.management.api.controller;

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

    @GetMapping("/get-by-id")
    public MenuItemsDto getMenuItemById(@RequestParam(value = "id") Long id){
        return menuItemsService.getMenuItemById(id);
    }

    @PutMapping
    public ResponseDto updateMenuItem(@RequestBody MenuItemsDto menuItemsDto){
        return menuItemsService.updateMenuItem(menuItemsDto);
    }

    @PutMapping("/update-status")
    public ResponseDto updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "status") String status){
        return menuItemsService.updateStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteMenuItem(@RequestParam(value = "id") Long id){
        return menuItemsService.deleteMenuItem(id);
    }
}

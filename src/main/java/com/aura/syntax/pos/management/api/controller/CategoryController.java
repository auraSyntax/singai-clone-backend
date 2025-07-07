package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.CategoryDto;
import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseDto addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/list")
    public List<CategoryDto> getAllCategories(@RequestParam(value = "mainCategoryId",required = false) Long mainCategoryId){
        return categoryService.getAllCategories(mainCategoryId);
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<CategoryDto> getAllCategoriesPagination(@RequestParam(value = "page") Integer page,
                                                                         @RequestParam(value = "size") Integer size,
                                                                         @RequestParam(value = "search") String search,
                                                                        @RequestParam(value = "mainCategoryId",required = false) Long mainCategoryId){
        return categoryService.getAllCategoriesPagination(page,size,search,mainCategoryId);
    }

    @GetMapping("/get-by-id")
    public CategoryDto getCategoryById(@RequestParam(value = "id") Long id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping
    public ResponseDto updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    @PutMapping("/update-status")
    public ResponseDto updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "status") String status){
        return categoryService.updateStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteMainCategory(@RequestParam(value = "id") Long id){
        return categoryService.deleteMainCategory(id);
    }
}

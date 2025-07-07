package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.MainCategoryDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.service.MainCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/main-category")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;

    @PostMapping
    public ResponseDto addMainCategory(@RequestBody MainCategoryDto mainCategoryDto){
        return mainCategoryService.addMainCategory(mainCategoryDto);
    }

    @GetMapping("/list")
    public List<MainCategoryDto> getAllMainCategories(){
        return mainCategoryService.getAllMainCategories();
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<MainCategoryDto> getAllMainCategoriesPagination(@RequestParam(value = "page") Integer page,
                                                                                @RequestParam(value = "size") Integer size,
                                                                                @RequestParam(value = "search") String search){
        return mainCategoryService.getAllMainCategoriesPagination(page,size,search);
    }

    @GetMapping("/get-by-id")
    public MainCategoryDto getMainCategoryById(@RequestParam(value = "id") Long id){
        return mainCategoryService.getMainCategoryById(id);
    }

    @PutMapping
    public ResponseDto updateMainCategory(@RequestBody MainCategoryDto mainCategoryDto){
        return mainCategoryService.updateMainCategory(mainCategoryDto);
    }

    @PutMapping("/update-status")
    public ResponseDto updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "status") String status){
        return mainCategoryService.updateStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteMainCategory(@RequestParam(value = "id") Long id){
        return mainCategoryService.deleteMainCategory(id);
    }

}

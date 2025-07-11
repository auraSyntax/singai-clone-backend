package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.CategoryDto;
import com.aura.syntax.pos.management.api.dto.IngredientsDto;
import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.service.CategoryService;
import com.aura.syntax.pos.management.service.IngredientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ingredients")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class IngredientsController {

    private final IngredientsService ingredientsService;

    @PostMapping
    public ResponseDto addIngredient(@RequestBody IngredientsDto ingredientsDto){
        return ingredientsService.addIngredient(ingredientsDto);
    }

    @GetMapping("/list")
    public List<IngredientsDto> getAllIngredients(){
        return ingredientsService.getAllIngredients();
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<IngredientsDto> getAllIngredientsPagination(@RequestParam(value = "page") Integer page,
                                                                        @RequestParam(value = "size") Integer size,
                                                                        @RequestParam(value = "search",required = false) String search){
        return ingredientsService.getAllIngredientsPagination(page,size,search);
    }

    @GetMapping("/get-by-id")
    public IngredientsDto getIngredientById(@RequestParam(value = "id") Long id){
        return ingredientsService.getIngredientById(id);
    }

    @PutMapping
    public ResponseDto updateIngredient(@RequestBody IngredientsDto ingredientsDto){
        return ingredientsService.updateIngredient(ingredientsDto);
    }

    @DeleteMapping
    public ResponseDto deleteIngredient(@RequestParam(value = "id") Long id){
        return ingredientsService.deleteIngredient(id);
    }
}

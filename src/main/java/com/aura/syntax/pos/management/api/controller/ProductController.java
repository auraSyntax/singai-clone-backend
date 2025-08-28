package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ProductDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","https://singai-pos.onrender.com","https://astounding-monstera-9f8e37.netlify.app"})
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseDto addProduct(@RequestBody ProductDto productDto){
        return productService.addProduct(productDto);
    }

    @GetMapping("get-by-id")
    public ProductDto getStockItemById(@RequestParam(value = "id") Long id){
        return productService.getProductById(id);
    }

    @PutMapping
    public ResponseDto updateProduct(@RequestBody ProductDto productDto){
        return productService.updateProduct(productDto);
    }

    @GetMapping("/get-all-pagination")
    public PaginatedResponseDto<ProductDto> getAllProducts(@RequestParam(value = "page") Integer page,
                                                                @RequestParam(value = "size") Integer size,
                                                                @RequestParam(value = "search",required = false) String search){
        return productService.getAllProducts(page,size,search);
    }

    @PutMapping("/update-status")
    public ResponseDto updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "status") Boolean status){
        return productService.updateStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteStockItem(@RequestParam(value = "id") Long id){
        return productService.deleteProduct(id);
    }
}

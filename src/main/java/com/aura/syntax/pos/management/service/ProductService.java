package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ProductDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.StockItemsDto;
import com.aura.syntax.pos.management.entity.MenuItems;
import com.aura.syntax.pos.management.entity.Product;
import com.aura.syntax.pos.management.entity.StockItems;
import com.aura.syntax.pos.management.enums.Type;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.MenuItemsRepository;
import com.aura.syntax.pos.management.repository.ProductRepository;
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
public class ProductService {
    
    private final ProductRepository productRepository;
    
    private final MenuItemsRepository menuItemsRepository;
    
    public ResponseDto addProduct(ProductDto productDto) {

        if (productDto.getType().equalsIgnoreCase(Type.RETAIL_ITEM.getMappedValue())){
            MenuItems menuItems = MenuItems.builder()
                    .name(productDto.getProductName())
                    .categoryId(productDto.getCategoryId())
                    .description(productDto.getDescription())
                    .imageUrl(productDto.getImageUrl())
                    .price(productDto.getPrice())
                    .build();
            menuItemsRepository.save(menuItems);
        }
        Product product = Product.builder()
                .id(productDto.getId())
                .productName(productDto.getProductName())
                .minimumStock(productDto.getMinimumStock())
                .currentStock(productDto.getCurrentStock())
                .type(Type.fromMappedValue(productDto.getType()))
                .createdAt(LocalDateTime.now())
                .isActive(Boolean.TRUE)
                .build();
        productRepository.save(product);
        return new ResponseDto("Stock Item Saved");
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Product not found","Bad request", HttpStatus.BAD_REQUEST));
        return ProductDto.builder()
                .id(product.getId())
                .type(product.getType().getMappedValue())
                .minimumStock(product.getMinimumStock())
                .productName(product.getProductName())
                .currentStock(product.getCurrentStock())
                .isActive(product.getIsActive())
                .build();
    }

    public ResponseDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new ServiceException("Product not found","Bad request", HttpStatus.BAD_REQUEST));
        product.setId(productDto.getId());
        product.setCurrentStock(productDto.getCurrentStock());
        product.setMinimumStock(productDto.getMinimumStock());
        product.setProductName(productDto.getProductName());
        product.setType(Type.fromMappedValue(productDto.getType()));
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        return new ResponseDto("Product Updated");
    }

    public PaginatedResponseDto<ProductDto> getAllProducts(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<ProductDto> productDtos = productRepository.getAllProductItems(pageable,search);
        PaginatedResponseDto<ProductDto> productDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<ProductDto> productDtosList = productDtos.getContent();
        productDtoPaginatedResponseDto.setData(productDtosList);
        productDtoPaginatedResponseDto.setCurrentPage(page);
        productDtoPaginatedResponseDto.setTotalPages(productDtos.getTotalPages());
        productDtoPaginatedResponseDto.setTotalItems(productDtos.getTotalElements());

        return productDtoPaginatedResponseDto;
    }

    public ResponseDto updateStatus(Long id, Boolean status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Product not found","Bad request", HttpStatus.BAD_REQUEST));

        product.setIsActive(status);
        productRepository.save(product);
        return new ResponseDto("Product status updated successfully");
    }

    public ResponseDto deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Product not found","Bad request", HttpStatus.BAD_REQUEST));
        productRepository.deleteById(id);
        return new ResponseDto("Product deleted successfully");
    }
}

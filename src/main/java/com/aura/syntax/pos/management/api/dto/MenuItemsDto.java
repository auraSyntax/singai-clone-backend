package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.entity.OrderItems;
import com.aura.syntax.pos.management.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemsDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private String imageUrl;
    private String imageUrlWithDomain;
    private Integer preparationTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<MenuItemIncredientsDto> menuItemIncredientsDtos;
    private Set<OrderItemsDto> orderItemsDtos;
    private String categoryName;

    public MenuItemsDto(Long id, String name, String description, Double price, String imageUrl, Integer preparationTime,
                        Status status,Long categoryId,String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.preparationTime = preparationTime;
        this.status = status.getMappedValue();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}

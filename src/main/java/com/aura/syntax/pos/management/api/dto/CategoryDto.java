package com.aura.syntax.pos.management.api.dto;

import com.aura.syntax.pos.management.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    private Long id;
    private String categoryName;
    private String description;
    private String imageUrl;
    private String status;
    private Long mainCategoryId;

    public CategoryDto(Long id, String categoryName, String description, String imageUrl, Status status,Long mainCategoryId) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status.getMappedValue();
        this.mainCategoryId = mainCategoryId;
    }
}

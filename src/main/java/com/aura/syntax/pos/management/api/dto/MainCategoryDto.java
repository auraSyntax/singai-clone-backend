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
public class MainCategoryDto {
    private Long id;
    private String mainCategoryName;
    private String description;
    private String imageUrl;
    private String status;

    public MainCategoryDto(Long id, String mainCategoryName) {
        this.id = id;
        this.mainCategoryName = mainCategoryName;
    }

    public MainCategoryDto(Long id, String mainCategoryName, String description, String imageUrl, Status status) {
        this.id = id;
        this.mainCategoryName = mainCategoryName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status.getMappedValue();
    }
}

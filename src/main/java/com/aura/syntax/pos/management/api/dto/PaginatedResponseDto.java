package com.aura.syntax.pos.management.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponseDto<T> {
    private long totalItems;
    private List<T> data;
    private int totalPages;
    private int currentPage;
}
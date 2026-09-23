package com.portfolio.catalog.dto;

import com.portfolio.catalog.model.Category;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description
) {
    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getDescription());
    }
}
package com.portfolio.catalog.dto;

import com.portfolio.catalog.model.Category;
import java.io.Serializable;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description
) implements Serializable {

    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getDescription());
    }
}
package com.portfolio.catalog.dto;

import com.portfolio.catalog.model.Product;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        CategoryResponseDTO category
) {
    public ProductResponseDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                new CategoryResponseDTO(product.getCategory())
        );
    }
}
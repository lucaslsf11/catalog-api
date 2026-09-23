package com.portfolio.catalog.controller;

import com.portfolio.catalog.dto.ProductRequestDTO;
import com.portfolio.catalog.dto.ProductResponseDTO;
import com.portfolio.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponseDTO> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        ProductResponseDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> findByCategory(
            @PathVariable Long categoryId,
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponseDTO> products = productService.findByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO createdProduct = productService.create(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.id())
                .toUri();

        return ResponseEntity.created(uri).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO updatedProduct = productService.update(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.portfolio.catalog.service;

import com.portfolio.catalog.dto.ProductRequestDTO;
import com.portfolio.catalog.dto.ProductResponseDTO;
import com.portfolio.catalog.exception.ResourceNotFoundException;
import com.portfolio.catalog.model.Category;
import com.portfolio.catalog.model.Product;
import com.portfolio.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        return new ProductResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findByCategory(Long categoryId, Pageable pageable) {
        categoryService.findEntityById(categoryId); // Valida se a categoria existe
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(ProductResponseDTO::new);
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryService.findEntityById(dto.categoryId());

        Product product = new Product();
        mapDtoToEntity(dto, product, category);

        product = productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));

        Category category = categoryService.findEntityById(dto.categoryId());
        mapDtoToEntity(dto, product, category);

        product = productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com o ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private void mapDtoToEntity(ProductRequestDTO dto, Product entity, Category category) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setStock(dto.stock());
        entity.setCategory(category);
    }
}
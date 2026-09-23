package com.portfolio.catalog.service;

import com.portfolio.catalog.dto.CategoryRequestDTO;
import com.portfolio.catalog.dto.CategoryResponseDTO;
import com.portfolio.catalog.exception.BusinessException;
import com.portfolio.catalog.exception.ResourceNotFoundException;
import com.portfolio.catalog.model.Category;
import com.portfolio.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = findEntityById(id);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Já existe uma categoria registada com o nome: " + dto.name());
        }

        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        category = categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findEntityById(id);

        categoryRepository.findByNameIgnoreCase(dto.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Já existe outra categoria registada com o nome: " + dto.name());
                });

        category.setName(dto.name());
        category.setDescription(dto.description());

        category = categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }

    public Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }
}
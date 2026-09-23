package com.portfolio.catalog.service;

import com.portfolio.catalog.dto.CategoryRequestDTO;
import com.portfolio.catalog.dto.CategoryResponseDTO;
import com.portfolio.catalog.exception.ResourceNotFoundException;
import com.portfolio.catalog.model.Category;
import com.portfolio.catalog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Informática", "Acessórios e componentes de computador");
        requestDTO = new CategoryRequestDTO("Informática", "Acessórios e componentes de computador");
    }

    @Test
    @DisplayName("Deve retornar lista de categorias com sucesso")
    void findAll_ShouldReturnListOfCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(category.getId(), result.get(0).id());
        assertEquals(category.getName(), result.get(0).name());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar categoria por ID quando existir")
    void findById_WhenIdExists_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Informática", result.name());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existir")
    void findById_WhenIdDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.findById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(categoryRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve salvar e retornar categoria com sucesso")
    void create_ShouldPersistAndReturnCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO result = categoryService.create(requestDTO);

        assertNotNull(result);
        assertEquals(category.getId(), result.id());
        assertEquals(category.getName(), result.name());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Deve deletar categoria quando ID existir")
    void delete_WhenIdExists_ShouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertDoesNotThrow(() -> categoryService.delete(1L));

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar ID inexistente")
    void delete_WhenIdDoesNotExist_ShouldThrowException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(99L));

        verify(categoryRepository, times(1)).findById(99L);
    }
}
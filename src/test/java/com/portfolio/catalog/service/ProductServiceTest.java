package com.portfolio.catalog.service;

import com.portfolio.catalog.dto.ProductRequestDTO;
import com.portfolio.catalog.dto.ProductResponseDTO;
import com.portfolio.catalog.exception.ResourceNotFoundException;
import com.portfolio.catalog.model.Category;
import com.portfolio.catalog.model.Product;
import com.portfolio.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductRequestDTO requestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Periféricos", "Acessórios de computador");
        product = new Product(1L, "Mouse Gamer", "Mouse 8500 DPI", new BigDecimal("150.00"), 20, category);
        requestDTO = new ProductRequestDTO("Mouse Gamer", "Mouse 8500 DPI", new BigDecimal("150.00"), 20, 1L);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve retornar produtos paginados com sucesso")
    void findAll_ShouldReturnPagedProducts() {
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<ProductResponseDTO> result = productService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Mouse Gamer", result.getContent().get(0).name());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void findById_WhenIdExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Mouse Gamer", result.name());
        assertEquals(new BigDecimal("150.00"), result.price());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID de produto inexistente")
    void findById_WhenIdDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(99L));
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve criar produto com categoria associada válida")
    void create_WhenCategoryExists_ShouldPersistProduct() {
        when(categoryService.findEntityById(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO result = productService.create(requestDTO);

        assertNotNull(result);
        assertEquals("Mouse Gamer", result.name());
        assertEquals(category.getId(), result.category().id());
        verify(categoryService, times(1)).findEntityById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao criar produto com categoria inexistente")
    void create_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(categoryService.findEntityById(1L))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada com o ID: 1"));

        assertThrows(ResourceNotFoundException.class, () -> productService.create(requestDTO));
        verify(categoryService, times(1)).findEntityById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve deletar produto quando ID existir")
    void delete_WhenIdExists_ShouldDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.delete(1L));

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar produto inexistente")
    void delete_WhenIdDoesNotExist_ShouldThrowException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.delete(99L));

        verify(productRepository, times(1)).existsById(99L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
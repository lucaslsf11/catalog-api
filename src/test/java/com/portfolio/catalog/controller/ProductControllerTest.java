package com.portfolio.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.catalog.dto.CategoryResponseDTO;
import com.portfolio.catalog.dto.ProductRequestDTO;
import com.portfolio.catalog.dto.ProductResponseDTO;
import com.portfolio.catalog.exception.GlobalExceptionHandler;
import com.portfolio.catalog.exception.ResourceNotFoundException;
import com.portfolio.catalog.service.ProductService;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponseDTO productResponseDTO;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Periféricos", "Acessórios de computador");
        productResponseDTO = new ProductResponseDTO(1L, "Mouse Gamer", "Mouse 8500 DPI", new BigDecimal("150.00"), 20, categoryDTO);
        productRequestDTO = new ProductRequestDTO("Mouse Gamer", "Mouse 8500 DPI", new BigDecimal("150.00"), 20, 1L);
    }

    @Test
    @DisplayName("GET /api/v1/products - Deve retornar 200 OK com página de produtos")
    void findAll_ShouldReturnPagedProducts() throws Exception {
        Page<ProductResponseDTO> page = new PageImpl<>(List.of(productResponseDTO), PageRequest.of(0, 10), 1);
        when(productService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Mouse Gamer"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(productService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/products/{id} - Deve retornar 200 OK quando o produto existir")
    void findById_WhenIdExists_ShouldReturnProduct() throws Exception {
        when(productService.findById(1L)).thenReturn(productResponseDTO);

        mockMvc.perform(get("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mouse Gamer"));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/products/{id} - Deve retornar 404 Not Found quando o produto não existir")
    void findById_WhenIdDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(productService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Produto não encontrado com o ID: 99"));

        mockMvc.perform(get("/api/v1/products/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Produto não encontrado com o ID: 99"));

        verify(productService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("GET /api/v1/products/category/{categoryId} - Deve retornar 200 OK com produtos da categoria")
    void findByCategory_ShouldReturnPagedProducts() throws Exception {
        Page<ProductResponseDTO> page = new PageImpl<>(List.of(productResponseDTO), PageRequest.of(0, 10), 1);
        when(productService.findByCategory(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/products/category/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].category.id").value(1L));

        verify(productService, times(1)).findByCategory(eq(1L), any(Pageable.class));
    }

    @Test
    @DisplayName("POST /api/v1/products - Deve retornar 201 Created ao enviar payload válido")
    void create_WhenPayloadIsValid_ShouldReturnCreated() throws Exception {
        when(productService.create(any(ProductRequestDTO.class))).thenReturn(productResponseDTO);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mouse Gamer"));

        verify(productService, times(1)).create(any(ProductRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/products/{id} - Deve retornar 204 No Content ao excluir")
    void delete_WhenIdExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(1L);
    }
}
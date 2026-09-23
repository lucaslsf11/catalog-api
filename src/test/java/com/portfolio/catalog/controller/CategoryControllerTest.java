package com.portfolio.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.catalog.dto.CategoryRequestDTO;
import com.portfolio.catalog.dto.CategoryResponseDTO;
import com.portfolio.catalog.exception.GlobalExceptionHandler;
import com.portfolio.catalog.exception.ResourceNotFoundException;
import com.portfolio.catalog.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/categories - Deve retornar 200 OK com lista de categorias")
    void findAll_ShouldReturnOk() throws Exception {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, "Informática", "Componentes de PC");
        when(categoryService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Informática"));

        verify(categoryService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/categories/{id} - Deve retornar 200 OK quando categoria existir")
    void findById_WhenIdExists_ShouldReturnOk() throws Exception {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, "Informática", "Componentes de PC");
        when(categoryService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Informática"));

        verify(categoryService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/categories/{id} - Deve retornar 404 Not Found quando ID não existir")
    void findById_WhenIdDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(categoryService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Categoria não encontrada com o ID: 99"));

        mockMvc.perform(get("/api/v1/categories/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Categoria não encontrada com o ID: 99"));

        verify(categoryService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /api/v1/categories - Deve retornar 201 Created ao enviar payload válido")
    void create_WhenPayloadIsValid_ShouldReturnCreated() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Informática", "Componentes de PC");
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(1L, "Informática", "Componentes de PC");

        when(categoryService.create(any(CategoryRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Informática"));

        verify(categoryService, times(1)).create(any(CategoryRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/categories/{id} - Deve retornar 204 No Content ao excluir com sucesso")
    void delete_WhenIdExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/api/v1/categories/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete(1L);
    }
}
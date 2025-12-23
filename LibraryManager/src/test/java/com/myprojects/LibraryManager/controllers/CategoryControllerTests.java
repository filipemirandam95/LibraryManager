package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import com.myprojects.LibraryManager.dto.category.CategoryResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.services.CategoryService;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private PageImpl<CategoryResponseDTO> categoryResponseDTOPage;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 900L;
        dependentId = 3L;
        categoryRequestDTO = Factory.createCategoryRequestDTO(existingId);
        categoryResponseDTO = Factory.createCategoryResponseDTO(existingId);
        categoryResponseDTOPage = new PageImpl<>(List.of(categoryResponseDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPage() throws Exception {
        when(categoryService.findAll(any())).thenReturn(categoryResponseDTOPage);

        ResultActions resultActions = mockMvc.perform(get("/categories")
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategoryShouldReturnCategoryResponseDTOWhenIdExists() throws Exception {
        when(categoryService.findById(existingId)).thenReturn(categoryResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/categories/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategoryShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(categoryService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(get("/categories/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void insertShouldReturnCategoryResponseDTO() throws Exception {
        when(categoryService.insert(any())).thenReturn(categoryResponseDTO);

        String jsonBody = objectMapper.writeValueAsString(categoryRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/categories")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnCategoryResponseDTOWhenIdExists() throws Exception {
        when(categoryService.update(eq(existingId), any())).thenReturn(categoryResponseDTO);
        String jsonBody = objectMapper.writeValueAsString(categoryRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/categories/{id}", existingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(categoryService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        String jsonBody = objectMapper.writeValueAsString(categoryRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/categories/{id}", nonExistingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(categoryService).delete(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/categories/{id}", existingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(categoryService).delete(nonExistingId);
        ResultActions resultActions = mockMvc.perform(delete("/categories/{id}", nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(ResourceNotFoundException.class).when(categoryService).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/categories/{id}", dependentId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}


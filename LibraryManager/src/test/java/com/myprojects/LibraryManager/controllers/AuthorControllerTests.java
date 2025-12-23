package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.LibraryManager.dto.author.AuthorRequestDTO;
import com.myprojects.LibraryManager.dto.author.AuthorResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.services.AuthorService;
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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService authorService;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private AuthorRequestDTO authorRequestDTO;
    private AuthorResponseDTO authorResponseDTO;
    private PageImpl<AuthorResponseDTO> authorResponseDTOPage;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 900L;
        dependentId = 3L;
        authorRequestDTO = Factory.createAuthorRequestDTO(existingId);
        authorResponseDTO = Factory.createAuthorResponseDTO(existingId);
        authorResponseDTOPage = new PageImpl<>(List.of(authorResponseDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPage() throws Exception {
        when(authorService.findAll(any())).thenReturn(authorResponseDTOPage);

        ResultActions resultActions = mockMvc.perform(get("/authors")
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAuthorShouldReturnAuthorResponseDTOWhenIdExists() throws Exception {
        when(authorService.findById(existingId)).thenReturn(authorResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/authors/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAuthorShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(authorService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(get("/authors/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void insertShouldReturnAuthorResponseDTO() throws Exception {
        when(authorService.insert(any())).thenReturn(authorResponseDTO);

        String jsonBody = objectMapper.writeValueAsString(authorRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/authors")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnAuthorResponseDTOWhenIdExists() throws Exception {
        when(authorService.update(eq(existingId), any())).thenReturn(authorResponseDTO);
        String jsonBody = objectMapper.writeValueAsString(authorRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/authors/{id}", existingId)
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
        when(authorService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        String jsonBody = objectMapper.writeValueAsString(authorRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/authors/{id}", nonExistingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(authorService).delete(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/authors/{id}", existingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(authorService).delete(nonExistingId);
        ResultActions resultActions = mockMvc.perform(delete("/authors/{id}", nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(ResourceNotFoundException.class).when(authorService).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/authors/{id}", dependentId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}



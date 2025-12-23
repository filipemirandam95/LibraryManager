package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.LibraryManager.dto.fine.FineRequestDTO;
import com.myprojects.LibraryManager.dto.fine.FineResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.services.FineService;
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


@WebMvcTest(FineController.class)
public class FineControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FineService fineService;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private FineRequestDTO fineRequestDTO;
    private FineResponseDTO fineResponseDTO;
    private PageImpl<FineResponseDTO> fineResponseDTOPage;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 900L;
        dependentId = 3L;
        fineRequestDTO = Factory.createFineRequestDTO(existingId);
        fineResponseDTO = Factory.createFineResponseDTO(existingId);
        fineResponseDTOPage = new PageImpl<>(List.of(fineResponseDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPage() throws Exception {
        when(fineService.findAll(any())).thenReturn(fineResponseDTOPage);

        ResultActions resultActions = mockMvc.perform(get("/fines")
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFineShouldReturnFineResponseDTOWhenIdExists() throws Exception {
        when(fineService.findById(existingId)).thenReturn(fineResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/fines/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFineShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(fineService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(get("/fines/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void insertShouldReturnFineResponseDTO() throws Exception {
        when(fineService.insert(any())).thenReturn(fineResponseDTO);

        String jsonBody = objectMapper.writeValueAsString(fineRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/fines")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnFineResponseDTOWhenIdExists() throws Exception {
        when(fineService.update(eq(existingId), any())).thenReturn(fineResponseDTO);
        String jsonBody = objectMapper.writeValueAsString(fineRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/fines/{id}", existingId)
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
        when(fineService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        String jsonBody = objectMapper.writeValueAsString(fineRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/fines/{id}", nonExistingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(fineService).delete(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/fines/{id}", existingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(fineService).delete(nonExistingId);
        ResultActions resultActions = mockMvc.perform(delete("/fines/{id}", nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(ResourceNotFoundException.class).when(fineService).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/fines/{id}", dependentId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}


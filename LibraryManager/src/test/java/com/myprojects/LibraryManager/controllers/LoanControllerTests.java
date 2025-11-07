package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.LibraryManager.dto.loan.LoanRequestDTO;
import com.myprojects.LibraryManager.dto.loan.LoanResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.services.LoanService;
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


@WebMvcTest(LoanController.class)
public class LoanControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LoanService loanService;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private LoanRequestDTO loanRequestDTO;
    private LoanResponseDTO loanResponseDTO;
    private PageImpl<LoanResponseDTO> loanResponseDTOPage;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 900L;
        dependentId = 3L;
        loanRequestDTO = Factory.createLoanRequestDTO(existingId);
        loanResponseDTO = Factory.createLoanResponseDTO(existingId);
        loanResponseDTOPage = new PageImpl<>(List.of(loanResponseDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPage() throws Exception {
        when(loanService.findAll(any())).thenReturn(loanResponseDTOPage);

        ResultActions resultActions = mockMvc.perform(get("/loans")
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLoanShouldReturnLoanResponseDTOWhenIdExists() throws Exception {
        when(loanService.findById(existingId)).thenReturn(loanResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/loans/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getLoanShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(loanService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        ResultActions resultActions = mockMvc.perform(get("/loans/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void insertShouldReturnLoanResponseDTO() throws Exception {
        when(loanService.insert(any())).thenReturn(loanResponseDTO);

        String jsonBody = objectMapper.writeValueAsString(loanRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/loans")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnLoanResponseDTOWhenIdExists() throws Exception {
        when(loanService.update(eq(existingId), any())).thenReturn(loanResponseDTO);
        String jsonBody = objectMapper.writeValueAsString(loanRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/loans/{id}", existingId)
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
        when(loanService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        String jsonBody = objectMapper.writeValueAsString(loanRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/loans/{id}", nonExistingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(loanService).delete(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/loans/{id}", existingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(loanService).delete(nonExistingId);
        ResultActions resultActions = mockMvc.perform(delete("/loans/{id}", nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(ResourceNotFoundException.class).when(loanService).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/loans/{id}", dependentId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}


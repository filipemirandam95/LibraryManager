package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.LibraryManager.dto.reservation.ReservationRequestDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.services.ReservationService;
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

@WebMvcTest(ReservationController.class)
public class ReservationControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ReservationRequestDTO reservationRequestDTO;
    private ReservationResponseDTO reservationResponseDTO;
    private PageImpl<ReservationResponseDTO> reservationResponseDTOPage;


    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 900L;
        dependentId = 3L;
        reservationRequestDTO = Factory.createReservationRequestDTO(existingId);
        reservationResponseDTO = Factory.createReservationResponseDTO(existingId);
        reservationResponseDTOPage = new PageImpl<>(List.of(reservationResponseDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPage() throws Exception {
        when(reservationService.findAll(any())).thenReturn(reservationResponseDTOPage);

        ResultActions resultActions = mockMvc.perform(get("/reservations")
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getReservationShouldReturnReservationResponseDTOWhenIdExists() throws Exception {
        when(reservationService.findById(existingId)).thenReturn(reservationResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/reservations/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getReservationShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(reservationService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get("/reservations/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void insertShouldReturnReservationResponseDTO() throws Exception {
        when(reservationService.insert(any())).thenReturn(reservationResponseDTO);

        String jsonBody = objectMapper.writeValueAsString(reservationRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/reservations")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnReservationResponseDTOWhenIdExists() throws Exception {
        when(reservationService.update(eq(existingId), any())).thenReturn(reservationResponseDTO);
        String jsonBody = objectMapper.writeValueAsString(reservationRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/reservations/{id}", existingId)
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
        when(reservationService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        String jsonBody = objectMapper.writeValueAsString(reservationRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/reservations/{id}", nonExistingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(reservationService).delete(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/reservations/{id}", existingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(reservationService).delete(nonExistingId);
        ResultActions resultActions = mockMvc.perform(delete("/reservations/{id}", nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(ResourceNotFoundException.class).when(reservationService).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/reservations/{id}", dependentId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}

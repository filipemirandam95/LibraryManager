package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.LibraryManager.dto.user.UserRequestDTO;
import com.myprojects.LibraryManager.dto.user.UserResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.services.UserService;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private UserResponseDTO userResponseDTO;
    private UserRequestDTO userRequestDTO;
    private PageImpl<UserResponseDTO> userResponseDTOPage;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 900L;
        dependentId = 3L;
        userResponseDTO = Factory.createUserResponseDTO();
        userRequestDTO = Factory.createUserRequestDTO();
        userResponseDTOPage = new PageImpl<>(List.of(userResponseDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPage() throws Exception {

        when(userService.findAll(any()))
                .thenReturn(userResponseDTOPage);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserShouldReturnUserResponseDTOWhenIdExists() throws Exception {

        when(userService.findById(existingId)).thenReturn(userResponseDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userResponseDTO.getId()));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userResponseDTO.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        when(userService.findById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Resource not found"));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void insertShouldReturnUserResponseDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userRequestDTO);

        when(userService.insert(any(UserRequestDTO.class)))
                .thenReturn(userResponseDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnUserResponseDTOWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userRequestDTO);
        when(userService.update(eq(existingId), any(UserRequestDTO.class)))
                .thenReturn(userResponseDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", existingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userRequestDTO);

        when(userService.update(eq(nonExistingId), any(UserRequestDTO.class)))
                .thenThrow(ResourceNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", nonExistingId)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        Mockito.doNothing().when(userService).delete(existingId);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", existingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(userService).delete(nonExistingId);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(userService).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", dependentId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

package com.myprojects.LibraryManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Setup code if needed
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPageWhenBookNameParamIsEmpty() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/books")
                .accept("application/json"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content[0].id").value(1L));
        resultActions.andExpect(jsonPath("$.content[0].title").value("O Senhor dos Anéis"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAllShouldReturnPageWhenBookNameParamIsNotEmpty() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/books?title={title}", "O Senhor dos Anéis")
                .accept("application/json"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content[0].id").value(1L));
        resultActions.andExpect(jsonPath("$.content[0].title").value("O Senhor dos Anéis"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findByIdShouldReturnBookResponseDTOWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/books/{id}", 2L)
                .accept("application/json"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(2L));
        resultActions.andExpect(jsonPath("$.title").value("Harry Potter e a Pedra Filosofal"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/books/{id}", 999L)
                .accept("application/json"));

        resultActions.andExpect(status().isNotFound());
    }
}

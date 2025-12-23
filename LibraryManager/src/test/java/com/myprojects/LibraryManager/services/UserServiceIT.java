package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.user.UserResponseDTO;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIT {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalUsers;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 11L;
        nonExistingId = 1000L;
        countTotalUsers = 11L;
    }

    @Test
    public void deleteShouldDeleteUserWhenIdExists() {
        userService.delete(existingId);
        assertEquals(countTotalUsers - 1, userRepository.count());
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.delete(nonExistingId);
        });
    }

    @Test
    void findAllShouldReturnPageWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<UserResponseDTO> result = userService.findAll(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(countTotalUsers, result.getTotalElements());

    }

    @Test
    void findAllShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<UserResponseDTO> result = userService.findAll(pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<UserResponseDTO> result = userService.findAll(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Ana Souza", result.getContent().get(0).getName());
        assertEquals("Carlos Pereira", result.getContent().get(1).getName());
    }
}

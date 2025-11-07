package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.user.UserRequestDTO;
import com.myprojects.LibraryManager.dto.user.UserResponseDTO;
import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ForbiddenException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.UserRepository;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @Spy
    @InjectMocks
    private UserService userService;
    @Mock
    UserRepository userRepository;

    private User user;
    private UserRequestDTO userRequestDTO;
    private Long validUserId;
    private Long nonExistingUserId;

    @BeforeEach
    void setUp() throws Exception {
        user = Factory.createUserOperator();
        userRequestDTO = Factory.createUserRequestDTO();
        validUserId = 1L;
        nonExistingUserId = 500L;

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    public void findByIdShouldReturnUserResponseDTOWhenIdExistsAndUserIsAdmin() {
        User userAdmin = Factory.createUserAdministrator();
        Mockito.when(userRepository.findById(validUserId)).thenReturn(Optional.of(userAdmin));
        Mockito.doReturn(userAdmin).when(userService).authenticated();

        UserResponseDTO userResponseDTO = userService.findById(validUserId);

        assertNotNull(userResponseDTO);
    }

    @Test
    public void findByIdShouldReturnUserResponseDTOWhenIdExistsAndUserIsOwnerAndOperator() {
        Mockito.doReturn(user).when(userService).authenticated();
        UserResponseDTO userResponseDTO = userService.findById(validUserId);

        assertNotNull(userResponseDTO);
        assertEquals(user.getId(), userResponseDTO.getId());
        assertEquals(user.getName(), userResponseDTO.getName());
        assertEquals(user.getEmail(), userResponseDTO.getEmail());
        assertEquals(user.getBirthDate(), userResponseDTO.getBirthDate());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(userRepository).findById(nonExistingUserId);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(nonExistingUserId);
        });

        verify(userRepository).findById(nonExistingUserId);
    }

    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenIdExistsAndUserIsOperatorAndNotOwner() {
        User userNotOwner = Factory.createUserOperator();
        userNotOwner.setId(15L);
        doReturn(userNotOwner).when(userService).authenticated(); // user has id = 1L and is Operator;

        assertThrows(ForbiddenException.class, () -> {
            userService.findById(validUserId);
        });

        verify(userRepository).findById(validUserId);

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        when(userRepository.existsById(validUserId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(validUserId);

        Assertions.assertDoesNotThrow(() -> {
            userService.delete(validUserId);
        });

        verify(userRepository).deleteById(validUserId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(userRepository.existsById(nonExistingUserId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.delete(nonExistingUserId);
        });

        verify(userRepository).existsById(nonExistingUserId);
    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationException() {
        Long dependentUserId = 5L;
        when(userRepository.existsById(dependentUserId)).thenReturn(true);
        doThrow(DatabaseException.class).when(userRepository).deleteById(dependentUserId);

        assertThrows(DatabaseException.class, () -> {
            userService.delete(dependentUserId);
        });

        verify(userRepository).existsById(dependentUserId);
        verify(userRepository).deleteById(dependentUserId);
    }

    @Test
    public void insertShouldReturnUserResponseDTOWhenUserIsSaved() {
        UserResponseDTO userResponseDTO = userService.insert(userRequestDTO);

        assertNotNull(userResponseDTO);
        assertEquals(user.getId(), userResponseDTO.getId());
        assertEquals(user.getName(), userResponseDTO.getName());
        assertEquals(user.getEmail(), userResponseDTO.getEmail());
        assertEquals(user.getBirthDate(), userResponseDTO.getBirthDate());
        verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void insertShouldThrowDatabaseExceptionWhenDuplicateEmail() {
        String duplicateEmail = "joao@email.com";
        String password = "123456";
        LocalDate birthDate = LocalDate.of(1991, 01, 01);

        UserRequestDTO userRequestDTOWithDuplicateEmail = Factory.createUserRequestDTO(duplicateEmail, password, birthDate);

        doThrow(DatabaseException.class).when(userRepository).save(argThat(
                u -> u.getEmail().equals(duplicateEmail)
        ));

        assertThrows(DatabaseException.class, () -> {
            userService.insert(userRequestDTOWithDuplicateEmail);
        });

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void updateShouldReturnUserResponseDTOWhenUserExists() {
        when(userRepository.getReferenceById(validUserId)).thenReturn(user);

        UserResponseDTO userResponseDTO = userService.update(validUserId, userRequestDTO);

        assertNotNull(userResponseDTO);
        assertEquals(user.getId(), userResponseDTO.getId());
        assertEquals(user.getName(), userResponseDTO.getName());
        assertEquals(user.getEmail(), userResponseDTO.getEmail());
        assertEquals(user.getBirthDate(), userResponseDTO.getBirthDate());
        verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(userRepository).getReferenceById(nonExistingUserId);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.update(nonExistingUserId, userRequestDTO);
        });

        verify(userRepository).getReferenceById(nonExistingUserId);
    }
}

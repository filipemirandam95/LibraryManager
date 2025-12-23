package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.exceptions.ForbiddenException;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;

    private Long selfUserId;
    private Long randomUserId;
    private User selfUser;
    private User otherUser;

    @BeforeEach
    void setUp() {
        selfUserId = 1L;
        randomUserId = 12L;
        selfUser = Factory.createUser(selfUserId);
        otherUser = Factory.createUser(2L);

    }

    @Test
    void validateSelfOrAdminShouldDoNothingWhenUserIsAdminOrSelf() {
        when(userService.authenticated()).thenReturn(selfUser);

        assertDoesNotThrow(() ->
        {
            authService.validateSelfOrAdmin(selfUserId);
        });

        verify(userService).authenticated();

    }

    @Test
    void validateSelfOrAdminShouldThrowForbiddenExceptionWhenUserIsNotAdminOrSelf() {
        when(userService.authenticated()).thenReturn(otherUser);

        assertThrows(ForbiddenException.class, () -> {
            authService.validateSelfOrAdmin(randomUserId);
        });

        verify(userService).authenticated();

    }
}

package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.projections.UserDetailsProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private String validEmail;
    private String invalidEmail;
    private String validName;
    private String invalidName;
    private List<User> userListMock;

    @BeforeEach
    public void setUp() {
        validEmail = "juliana.santos@example.com";
        invalidEmail = "user@email.com";
        validName = "maria";
        invalidName = "invalidUserName";
    }

    @Test
    public void findUserLoansReservationsShouldReturnListWhenValidList() {

    }

    @Test
    public void searchUserAndRolesByEmailShouldReturnListWhenValidEmail() {
        List<UserDetailsProjection> userDetailsProjectionList = userRepository.searchUserAndRolesByEmail(validEmail);
        Assertions.assertFalse(userDetailsProjectionList.isEmpty());
    }

    @Test
    public void searchUserAndRolesByEmailShouldReturnEmptyListWhenInvalidEmail() {
        List<UserDetailsProjection> userDetailsProjectionList = userRepository.searchUserAndRolesByEmail(invalidEmail);
        Assertions.assertTrue(userDetailsProjectionList.isEmpty());
    }

}

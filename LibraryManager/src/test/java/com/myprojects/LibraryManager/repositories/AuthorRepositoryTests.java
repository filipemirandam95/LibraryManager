package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class AuthorRepositoryTests {

    @Autowired
    private AuthorRepository authorRepository;
    private String validName;
    private String invalidName;

    @BeforeEach
    public void setUp() {
        validName = "J.R.R. Tolkien";
        invalidName = "invalidAuthorName";
    }

    @Test
    public void findByNameShouldReturnAuthorWhenValidName() {
        Optional<Author> author = authorRepository.findByName(validName);
        Assertions.assertTrue(author.isPresent());
    }

    @Test
    public void findByNameShouldReturnAuthorWhenInvalidName() {
        Optional<Author> author = authorRepository.findByName(invalidName);
        Assertions.assertTrue(author.isEmpty());
    }


}

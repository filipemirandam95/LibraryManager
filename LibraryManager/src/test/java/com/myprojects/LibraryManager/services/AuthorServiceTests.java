package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.author.AuthorResponseDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.AuthorRepository;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthorServiceTests {

    @InjectMocks
    private AuthorService authorService;
    @Mock
    private AuthorRepository authorRepository;

    private Long validAuthorId;
    private Long invalidAuthorId;
    private Long dependentAuthorId;
    private Author validAuthor;

    @BeforeEach
    void setUp() {
        validAuthorId = 1L;
        invalidAuthorId = 900L;
        dependentAuthorId = 10L;
        validAuthor = Factory.createAuthor(validAuthorId);

    }

    @Test
    void findByIdShouldReturnAuthorResponseDTOWhenIdExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(validAuthor));

        AuthorResponseDTO authorResponseDTO = authorService.findById(validAuthorId);

        assertNotNull(authorResponseDTO);
        verify(authorRepository).findById(validAuthorId);

    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(authorRepository).findById(invalidAuthorId);

        assertThrows(ResourceNotFoundException.class,
                () -> authorService.findById(invalidAuthorId));
        verify(authorRepository).findById(invalidAuthorId);
    }

    @Test
    void updateShouldReturnAuthorResponseDTOWhenAuthorExists() {
        when(authorRepository.getReferenceById(validAuthorId)).thenReturn(validAuthor);
        when(authorRepository.save(any(Author.class))).thenReturn(validAuthor);

        AuthorResponseDTO authorResponseDTO = authorService.update(validAuthorId, Factory.createAuthorRequestDTO(validAuthorId));

        assertNotNull(authorResponseDTO);
        verify(authorRepository).getReferenceById(validAuthorId);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenAuthorDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(authorRepository).getReferenceById(invalidAuthorId);

        assertThrows(ResourceNotFoundException.class,
                () -> authorService.update(invalidAuthorId, Factory.createAuthorRequestDTO(invalidAuthorId)));
        verify(authorRepository).getReferenceById(invalidAuthorId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(authorRepository.existsById(invalidAuthorId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> authorService.delete(invalidAuthorId));


    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenAuthorIdIsDependent() {
        when(authorRepository.existsById(dependentAuthorId)).thenReturn(true);
        doThrow(DatabaseException.class).when(authorRepository).deleteById(dependentAuthorId);

        assertThrows(DatabaseException.class,
                () -> authorService.delete(dependentAuthorId));
        verify(authorRepository).existsById(dependentAuthorId);
        verify(authorRepository).deleteById(dependentAuthorId);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        when(authorRepository.existsById(validAuthorId)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(validAuthorId);

        authorService.delete(validAuthorId);

        verify(authorRepository).existsById(validAuthorId);
        verify(authorRepository).deleteById(validAuthorId);
    }

    @Test
    void insertShouldReturnAuthorResponseDTOWhenAuthorIsValid() {
        when(authorRepository.save(any(Author.class))).thenReturn(validAuthor);

        AuthorResponseDTO authorResponseDTO = authorService.insert(Factory.createAuthorRequestDTO(validAuthorId));

        assertNotNull(authorResponseDTO);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void insertShouldThrowDatabaseExceptionWhenDuplicateAuthor() {
        when(authorRepository.save(any(Author.class)))
                .thenThrow(DatabaseException.class);

        assertThrows(DatabaseException.class,
                () -> authorService.insert(Factory.createAuthorRequestDTO(validAuthorId)));
        verify(authorRepository).save(any(Author.class));
    }

}

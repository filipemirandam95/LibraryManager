package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.book.BookRequestDTO;
import com.myprojects.LibraryManager.dto.book.BookResponseDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Category;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.AuthorRepository;
import com.myprojects.LibraryManager.repositories.BookRepository;
import com.myprojects.LibraryManager.repositories.CategoryRepository;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BookServiceTests {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AuthorRepository authorRepository;

    private long validBookId;
    private long dependentBookId;
    private long validCategoryId;
    private long validAuthorId;
    private long invalidBookId;
    private long invalidCategoryId;
    private long invalidAuthorId;
    private BookRequestDTO validBookRequestDTO;
    private BookRequestDTO invalidBookRequestDTO;
    private BookResponseDTO bookResponseDTO;
    private Book validBook;
    private Category validCategory;
    private Author validAuthor;

    @BeforeEach
    public void setUp() {
        validBookId = 1L;
        validCategoryId = 1L;
        validAuthorId = 1L;
        invalidBookId = 900L;
        invalidCategoryId = 900L;
        invalidAuthorId = 900L;

        validBook = Factory.createBook(validBookId);
        validBookRequestDTO = Factory.createBookRequestDTO(validBookId);
        invalidBookRequestDTO = Factory.createBookRequestDTO(invalidBookId);
        bookResponseDTO = Factory.createBookResponseDTO(validBook);
        validCategory = Factory.createCategory(validCategoryId);
        validAuthor = Factory.createAuthor(validAuthorId);
    }

    @Test
    void findByIdShouldReturnBookResponseDTOWhenBookIdExists() {
        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(validBook));

        BookResponseDTO bookResponseDTO = bookService.findById(validBookId);

        assertNotNull(bookResponseDTO);
        verify(bookRepository).findById(validBookId);

    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenBookIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(bookRepository).findById(invalidBookId);

        assertThrows(ResourceNotFoundException.class, () -> bookService.findById(invalidBookId));

        verify(bookRepository).findById(invalidBookId);
    }

    @Test
    void insertShouldReturnBookResponseDTOWhenCategoryAndAuthorExists() {
        when(categoryRepository.findByName(any(String.class))).thenReturn(Optional.of(validCategory));
        when(authorRepository.findByName(any(String.class))).thenReturn(Optional.of(validAuthor));
        when(bookRepository.save(any(Book.class))).thenReturn(validBook);

        BookResponseDTO bookResponseDTO = bookService.insert(validBookRequestDTO);

        assertNotNull(bookResponseDTO);
        verify(categoryRepository).findByName(any(String.class));
        //verify(categoryRepository, times(0)).save(any(Category.class));
        // verify(authorRepository, times(0)).save(any(Author.class));
        verify(authorRepository).findByName(any(String.class));
        verify(bookRepository).save(any(Book.class));

    }

    @Test
    void insertShouldReturnBookResponseDTOWhenCategoryAndAuthorDoesNotExist() {
        when(categoryRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(authorRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        when(categoryRepository.save(any(Category.class))).thenReturn(validCategory);
        when(authorRepository.save(any(Author.class))).thenReturn(validAuthor);
        when(bookRepository.save(any(Book.class))).thenReturn(validBook);

        BookResponseDTO bookResponseDTO = bookService.insert(validBookRequestDTO);

        assertNotNull(bookResponseDTO);

        verify(categoryRepository).save(any(Category.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorRepository).save(any(Author.class));
        verify(bookRepository).save(any(Book.class));

    }

    @Test
    void updateShouldReturnBookResponseDTOWhenBookExists() {
        when(bookRepository.getReferenceById(validBookRequestDTO.getId())).thenReturn(validBook);
        when(bookRepository.save(any(Book.class))).thenReturn(validBook);

        BookResponseDTO bookResponseDTO = bookService.update(validBookRequestDTO.getId(), validBookRequestDTO);

        assertNotNull(bookResponseDTO);
        verify(bookRepository).getReferenceById(validBookRequestDTO.getId());
        verify(bookRepository).save(any(Book.class));

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenBookDoesNotExists() {
        doThrow(ResourceNotFoundException.class).when(bookRepository).getReferenceById(invalidBookRequestDTO.getId());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.update(invalidBookRequestDTO.getId(), invalidBookRequestDTO));

        verify(bookRepository).getReferenceById(invalidBookRequestDTO.getId());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void deleteShouldDoNothingWhenBookIdExists() {
        when(bookRepository.existsById(validBookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(validBookId);

        assertDoesNotThrow(() -> {
            bookService.delete(validBookId);
        });
        verify(bookRepository).existsById(validBookId);
        verify(bookRepository).deleteById(validBookId);

    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenBookIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(bookRepository).existsById(invalidBookId);

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.delete(invalidBookId);
        });
        verify(bookRepository).existsById(invalidBookId);
        verify(bookRepository, times(0)).deleteById(invalidBookId);

    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenBookHasDependency() {
        when(bookRepository.existsById(dependentBookId)).thenReturn(true);
        doThrow(RuntimeException.class).when(bookRepository).deleteById(dependentBookId);

        assertThrows(RuntimeException.class, () -> {
            bookService.delete(dependentBookId);
        });
        verify(bookRepository).existsById(dependentBookId);
        verify(bookRepository).deleteById(dependentBookId);

    }
}

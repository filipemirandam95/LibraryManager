package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.author.AuthorRequestDTO;
import com.myprojects.LibraryManager.dto.book.BookRequestDTO;
import com.myprojects.LibraryManager.dto.book.BookResponseDTO;
import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Category;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.AuthorRepository;
import com.myprojects.LibraryManager.repositories.BookRepository;
import com.myprojects.LibraryManager.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public BookResponseDTO findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new BookResponseDTO(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        Page<Book> result = bookRepository.findAll(pageable);
        return result.map(x -> new BookResponseDTO(x));
    }

    @Transactional
    public BookResponseDTO insert(BookRequestDTO bookRequestDTO) {
        Book book = new Book();
        DTOtoModel(bookRequestDTO, book);

        for (CategoryRequestDTO categoryRequestDTO : bookRequestDTO.getCategories()) {
            Category category = categoryRepository.findByName(categoryRequestDTO.getName())
                    .orElse(null);
            if (category == null) {
                category = new Category();
                category.setName(categoryRequestDTO.getName()); // popula os dados
                category = categoryRepository.save(category);
            }
            book.getCategories().add(category);
        }
        for (AuthorRequestDTO authorRequestDTO : bookRequestDTO.getAuthors()) {
            Author author = authorRepository.findByName(authorRequestDTO.getName())
                    .orElse(null);
            if (author == null) {
                author = new Author();
                author.setName(authorRequestDTO.getName());
                author.setBirthDate(authorRequestDTO.getBirthDate());
                author = authorRepository.save(author);
            }
            book.getAuthors().add(author);
        }
        book = bookRepository.save(book);
        return new BookResponseDTO(book);
    }

    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO bookRequestDTO) {
        try {
            Book book = bookRepository.getReferenceById(id);
            DTOtoModel(bookRequestDTO, book);
            book = bookRepository.save(book);
            return new BookResponseDTO(book);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            bookRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");

        }
    }

    private void DTOtoModel(BookRequestDTO bookRequestDTO, Book book) {
        if (bookRequestDTO.getTitle() != null) { //If the field is null it means that this field will not be changed.
            book.setTitle(bookRequestDTO.getTitle());
        }
        if (bookRequestDTO.getIsbn() != null) {
            book.setIsbn(bookRequestDTO.getIsbn());
        }
        if (bookRequestDTO.getEdition() != null) {
            book.setEdition(bookRequestDTO.getEdition());
        }
        if (bookRequestDTO.getTotalCopies() != 0) {
            book.setTotalCopies(bookRequestDTO.getTotalCopies());
        }
        if (bookRequestDTO.getAvailableCopies() != 0) {
            book.setAvailableCopies(bookRequestDTO.getAvailableCopies());
        } else {
            book.setAvailableCopies(bookRequestDTO.getTotalCopies());
        }
    }


    public List<BookResponseDTO> findAllBooksCustom(String query, boolean title, boolean author, boolean isbn) {
        List<Book> bookList = bookRepository.findAllBooksCustom(query.toLowerCase(), title, author, isbn);

        List<BookResponseDTO> bookResponseDTOList = new ArrayList<>();
        for (Book book : bookList) {
            bookResponseDTOList.add(new BookResponseDTO(book));
            System.out.println("livro: \n" + book.getTitle());
            System.out.println(book.getId());
            for (Author authorBook : book.getAuthors()) {
                System.out.println(authorBook.getName());
            }
            for (Category category : book.getCategories()) {
                System.out.println(category.getName());
            }
            System.out.println(book.getYear());
        }
        return bookResponseDTOList;
    }
}

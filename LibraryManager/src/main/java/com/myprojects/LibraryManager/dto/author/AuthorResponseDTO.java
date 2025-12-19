package com.myprojects.LibraryManager.dto.author;

import com.myprojects.LibraryManager.dto.book.BookSimpleDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.entities.Book;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AuthorResponseDTO {
    private long id;
    private String name;
    private LocalDate birthDate;
    private Set<BookSimpleDTO> books = new HashSet<BookSimpleDTO>();

    public AuthorResponseDTO(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.birthDate = author.getBirthDate();

        for (Book book : author.getBooks()) {
            books.add(new BookSimpleDTO(book));
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<BookSimpleDTO> getBooks() {
        return books;
    }
}

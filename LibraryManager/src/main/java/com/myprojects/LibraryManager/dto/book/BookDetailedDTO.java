package com.myprojects.LibraryManager.dto.book;

import com.myprojects.LibraryManager.dto.author.AuthorSimpleDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.entities.Book;

import java.util.HashSet;
import java.util.Set;

public class BookDetailedDTO {
    private long id;
    private String title;
    private String isbn;
    private int year;
    private String edition;
    private Set<AuthorSimpleDTO> authors = new HashSet<AuthorSimpleDTO>();

    public BookDetailedDTO(Book book) {
        id = book.getId();
        title = book.getTitle();
        isbn = book.getIsbn();
        year = book.getYear();
        edition = book.getEdition();
        for (Author author : book.getAuthors()) {
            authors.add(new AuthorSimpleDTO(author));
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public String getEdition() {
        return edition;
    }

    public Set<AuthorSimpleDTO> getAuthors() {
        return authors;
    }
}

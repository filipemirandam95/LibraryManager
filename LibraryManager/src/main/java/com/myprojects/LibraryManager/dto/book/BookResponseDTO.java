package com.myprojects.LibraryManager.dto.book;

import com.myprojects.LibraryManager.dto.author.AuthorSimpleDTO;
import com.myprojects.LibraryManager.dto.category.CategorySimpleDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Category;

import java.util.HashSet;
import java.util.Set;

public class BookResponseDTO {
    private long id;
    private String title;
    private String isbn;
    private int year;
    private String edition;
    private int totalCopies;
    private int availableCopies;
    private Set<AuthorSimpleDTO> authors = new HashSet<AuthorSimpleDTO>();
    private Set<CategorySimpleDTO> categories = new HashSet<CategorySimpleDTO>();

    public BookResponseDTO() {
    }

    public BookResponseDTO(Book book) {
        id = book.getId();
        title = book.getTitle();
        isbn = book.getIsbn();
        year = book.getYear();
        edition = book.getEdition();
        totalCopies = book.getTotalCopies();
        availableCopies = book.getAvailableCopies();
        for (Category category : book.getCategories()) {
            categories.add(new CategorySimpleDTO(category));
        }
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

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public Set<AuthorSimpleDTO> getAuthors() {
        return authors;
    }

    public Set<CategorySimpleDTO> getCategories() {
        return categories;
    }
}

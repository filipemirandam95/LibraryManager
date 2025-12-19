package com.myprojects.LibraryManager.dto.book;

import com.myprojects.LibraryManager.entities.Book;

public class BookSimpleDTO {
    private long id;
    private String title;
    private String isbn;
    private int year;
    private String edition;

    public BookSimpleDTO(Book book) {
        id = book.getId();
        title = book.getTitle();
        isbn = book.getIsbn();
        year = book.getYear();
        edition = book.getEdition();
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
}

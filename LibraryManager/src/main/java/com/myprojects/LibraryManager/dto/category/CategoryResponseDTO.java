package com.myprojects.LibraryManager.dto.category;

import com.myprojects.LibraryManager.dto.book.BookDetailedDTO;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Category;

import java.util.HashSet;
import java.util.Set;

public class CategoryResponseDTO {
    private long id;
    private String name;
    private Set<BookDetailedDTO> books = new HashSet<BookDetailedDTO>();

    public CategoryResponseDTO(Category category){
        id = category.getId();
        name = category.getName();

        for (Book book : category.getBooks()) {
            books.add(new BookDetailedDTO(book));
        }

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<BookDetailedDTO> getBooks() {
        return books;
    }
}

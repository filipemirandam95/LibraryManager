package com.myprojects.LibraryManager.dto.book;

import com.myprojects.LibraryManager.dto.OnCreate;
import com.myprojects.LibraryManager.dto.author.AuthorRequestDTO;
import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class BookRequestDTO {
    private long id;
    @Size(min = 3, max = 100, message = "Title must have between 3 and 40 characters", groups = OnCreate.class)
    @NotBlank(message = "Field required", groups = OnCreate.class)
    private String title;
    @Pattern(
            regexp = "(\\d{9}[\\dXx])|(\\d{13})",
            message = "ISBN must have 10 or 13 characters"
    )
    @NotBlank(message = "Field required", groups = OnCreate.class)
    private String isbn;
    private int year;
    private String edition;
    private int totalCopies;
    private int availableCopies;
    private Set<AuthorRequestDTO> authors = new HashSet<AuthorRequestDTO>();
    private Set<CategoryRequestDTO> categories = new HashSet<CategoryRequestDTO>();

    public BookRequestDTO() {
    }

    public BookRequestDTO(Long id) {
    }

    public BookRequestDTO(long id, String title, String isbn, int year, String edition, int totalCopies, int availableCopies) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.edition = edition;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;

    }

    public BookRequestDTO(Book book) {
        id = book.getId();
        title = book.getTitle();
        isbn = book.getIsbn();
        year = book.getYear();
        edition = book.getEdition();
        totalCopies = book.getTotalCopies();
        availableCopies = book.getAvailableCopies();
        for (Category category : book.getCategories()) {
            categories.add(new CategoryRequestDTO(category));
        }
        for (Author author : book.getAuthors()) {
            authors.add(new AuthorRequestDTO(author));
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

    public String getEdition() {

        return edition;
    }

    public int getTotalCopies() {

        return totalCopies;
    }

    public int getAvailableCopies() {

        return availableCopies;
    }

    public int getYear() {
        return year;
    }

    public Set<AuthorRequestDTO> getAuthors() {
        return authors;
    }

    public Set<CategoryRequestDTO> getCategories() {

        return categories;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", year=" + year +
                ", edition='" + edition + '\'' +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                ", authors=" + authors +
                ", categories=" + categories +
                '}';
    }
}

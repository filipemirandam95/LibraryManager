package com.myprojects.LibraryManager.dto.author;

import com.myprojects.LibraryManager.entities.Author;

import java.time.LocalDate;

public class AuthorSimpleDTO {
    private long id;
    private String name;
    private LocalDate birthDate;

    public AuthorSimpleDTO(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.birthDate = author.getBirthDate();
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
}

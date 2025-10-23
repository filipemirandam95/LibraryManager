package com.myprojects.LibraryManager.dto.author;

import com.myprojects.LibraryManager.entities.Author;

import java.time.LocalDate;


public class AuthorRequestDTO {
    private long id;
    private String name;
    private LocalDate birthDate;

    public AuthorRequestDTO() {

    }
    public AuthorRequestDTO(Long id,String name) {
        this.id = id;
        this.name = name;
    }

    public AuthorRequestDTO(long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public AuthorRequestDTO(Author author) {
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

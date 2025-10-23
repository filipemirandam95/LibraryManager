package com.myprojects.LibraryManager.dto.user;

import com.myprojects.LibraryManager.entities.User;

import java.time.LocalDate;

public class UserSimpleDTO {

    private Long id;
    private String name;
    private String email;;
    private LocalDate birthDate;

    public UserSimpleDTO() {
    }

    public UserSimpleDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}

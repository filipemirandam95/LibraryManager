package com.myprojects.LibraryManager.dto.user;

import com.myprojects.LibraryManager.entities.User;

import java.time.LocalDate;

public class UserRequestDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;

    public UserRequestDTO() {

    }

    public UserRequestDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
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

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}

package com.myprojects.LibraryManager.dto.user;

import com.myprojects.LibraryManager.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoggedUserDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private List<String> roles = new ArrayList<>();

    public LoggedUserDTO() {
    }

    public LoggedUserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        for(GrantedAuthority role : user.getRoles()){
            roles.add(role.getAuthority());
        }

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

    public List<String> getRoles() {
        return roles;
    }

}

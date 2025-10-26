package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public void validateSelfOrAdmin(long userId){
        User loggedUser = userService.authenticated();
        if(!loggedUser.hasRole("ROLE_ADMIN") && !loggedUser.getId().equals(userId)){
            throw new ForbiddenException("Access denied");
        }
    }
}

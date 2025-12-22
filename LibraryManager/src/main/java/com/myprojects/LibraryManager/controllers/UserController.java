package com.myprojects.LibraryManager.controllers;


import com.myprojects.LibraryManager.dto.OnCreate;
import com.myprojects.LibraryManager.dto.OnUpdate;
import com.myprojects.LibraryManager.dto.user.LoggedUserDTO;
import com.myprojects.LibraryManager.dto.user.UserRequestDTO;
import com.myprojects.LibraryManager.dto.user.UserResponseDTO;
import com.myprojects.LibraryManager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Controller for Users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Get user by id",
            description = "Get user by id.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            description = "Get the logged user.",
            summary = "Get the logged user.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/me", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<LoggedUserDTO> getLoggedUser() {
        return ResponseEntity.ok(userService.getLoggedUser());
    }

    @Operation(
            description = "Get user by name.",
            summary = "Get user by name.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/search", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @Operation(
            description = "Find all pageable users.",
            summary = "Find all pageable users.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> findAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(
            description = "Create a new user.",
            summary = "Create a new user.",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @PostMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> insertUsers(@Validated(OnCreate.class) @RequestBody UserRequestDTO userRequestDTO) {
        System.out.println(userRequestDTO.toString());
        UserResponseDTO userResponseDTO = userService.insert(userRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @Operation(
            description = "Update a user.",
            summary = "Update a user.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @PutMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.update(id, userRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(
            description = "Delete a user.",
            summary = "Delete a user if it is possible.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
            }
    )
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

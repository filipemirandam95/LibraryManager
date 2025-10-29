package com.myprojects.LibraryManager.controllers;


import com.myprojects.LibraryManager.dto.OnCreate;
import com.myprojects.LibraryManager.dto.OnUpdate;
import com.myprojects.LibraryManager.dto.fine.FineRequestDTO;
import com.myprojects.LibraryManager.dto.fine.FineResponseDTO;
import com.myprojects.LibraryManager.services.FineService;
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

import java.net.URI;

@RestController
@RequestMapping(value = "/fines")
@Tag(name = "Fines", description = "Controller for Fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @Operation(
            description = "Get fine by id.",
            summary = "Get fine by id.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<FineResponseDTO> getFine(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.findById(id));
    }

    @Operation(
            description = "Find all pageable fines.",
            summary = "Find all pageable fines.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<FineResponseDTO>> findAllFines(Pageable pageable) {
        return ResponseEntity.ok(fineService.findAll(pageable));
    }

    @Operation(
            description = "Create a new fine.",
            summary = "Create a new fine.",
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
    public ResponseEntity<FineResponseDTO> insertFine(@Validated(OnCreate.class) @RequestBody FineRequestDTO fineRequestDTO) {
        FineResponseDTO fineResponseDTO = fineService.insert(fineRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(fineResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(fineResponseDTO);
    }

    @Operation(
            description = "Update a fine.",
            summary = "Update a fine.",
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
    public ResponseEntity<FineResponseDTO> updateFine(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody FineRequestDTO fineRequestDTO) {
        FineResponseDTO fineResponseDTO = fineService.update(id, fineRequestDTO);
        return ResponseEntity.ok(fineResponseDTO);
    }

    @Operation(
            description = "Delete a fine.",
            summary = "Delete a fine if it is possible.",
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
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

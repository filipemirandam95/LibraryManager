package com.myprojects.LibraryManager.controllers;

import com.myprojects.LibraryManager.dto.OnCreate;
import com.myprojects.LibraryManager.dto.OnUpdate;
import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import com.myprojects.LibraryManager.dto.category.CategoryResponseDTO;
import com.myprojects.LibraryManager.services.CategoryService;
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
import java.util.List;


@RestController
@RequestMapping(value = "/categories")
@Tag(name = "Categories", description = "Controller for Categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(
            description = "Get category by id.",
            summary = "Get category by id.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(
            description = "Get category by Name.",
            summary = "Get category by Name.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<CategoryResponseDTO>> getCategory(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.findByName(name));
    }

    @Operation(
            description = "Find all pageable categories.",
            summary = "Find all pageable categories.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Page<CategoryResponseDTO>> findAllCategories(Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }

    @Operation(
            description = "Create a new Category.",
            summary = "Create a new Category.",
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
    public ResponseEntity<CategoryResponseDTO> insertCategory(@Validated(OnCreate.class) @RequestBody CategoryRequestDTO categoryRequestDTO) {
        System.out.println(categoryRequestDTO.toString());
        CategoryResponseDTO categoryResponseDTO = categoryService.insert(categoryRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoryResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryResponseDTO);
    }

    @Operation(
            description = "Update a category.",
            summary = "Update a category.",
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
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.update(id, categoryRequestDTO);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @Operation(
            description = "Delete a category.",
            summary = "Delete a category if it is possible.",
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
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

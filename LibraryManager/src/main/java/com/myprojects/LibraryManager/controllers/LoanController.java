package com.myprojects.LibraryManager.controllers;

import com.myprojects.LibraryManager.dto.OnCreate;
import com.myprojects.LibraryManager.dto.OnUpdate;
import com.myprojects.LibraryManager.dto.loan.LoanRequestDTO;
import com.myprojects.LibraryManager.dto.loan.LoanResponseDTO;
import com.myprojects.LibraryManager.services.LoanService;
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
@RequestMapping(value = "/loans")
@Tag(name = "Loans", description = "Controller for Loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Operation(
            description = "Get loan by id.",
            summary = "Get loan by id.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<LoanResponseDTO> getLoan(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @Operation(
            description = "Find all loans.",
            summary = "Find all loans.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(value = "/all", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<LoanResponseDTO>> findAllLoans() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @Operation(
            description = "Find all Pageable loans.",
            summary = "Find all Pageable loans.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping( value ="/page", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<LoanResponseDTO>> findAllPageableLoans(Pageable pageable) {
        return ResponseEntity.ok(loanService.findAll(pageable));
    }

    @Operation(
            description = "Find all opened loans.",
            summary = "Find all opened loans.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(value = "/opened", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<LoanResponseDTO>> findAllOpened() {
        return ResponseEntity.ok(loanService.findAllOpened());
    }

    @Operation(
            description = "Find overdue Loans.",
            summary = "Find overdue Loans.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/overdue", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<LoanResponseDTO>> getOverdueLoans() {
        return ResponseEntity.ok(loanService.findOverdueLoans());
    }

//    @Operation(
//            description = "Get most loaned Books in a period.",
//            summary = "Get most loaned Books in a period.",
//            responses = {
//                    @ApiResponse(description = "Ok", responseCode = "200"),
//                    @ApiResponse(description = "Not found", responseCode = "404"),
//            }
//    )
//    @GetMapping(value = "/most-loaned", produces = "application/json")
//    public ResponseEntity<List<LoanResponseDTO>> getMostLoanedBooks(
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam("limit") int limit) {
//        return ResponseEntity.ok(loanService.findMostLoanedBooksInPeriod(startDate, endDate, limit));
//    }

//    @Operation(
//            description = "Get returned loans with delay.",
//            summary = "Get returned loans with delay.",
//            responses = {
//                    @ApiResponse(description = "Ok", responseCode = "200"),
//                    @ApiResponse(description = "Not found", responseCode = "404"),
//            }
//    )
//    @GetMapping(value = "/returned-with-delay", produces = "application/json")
//    public ResponseEntity<List<LoanResponseDTO>> getReturnedLoansWithDelay() {
//        return ResponseEntity.ok(loanService.findReturnedLoansWithDelay());
//    }

    @Operation(
            description = "Find all pageable loans.",
            summary = "Find all pageable loans.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<LoanResponseDTO>> findAllLPageableLoans(Pageable pageable) {
        return ResponseEntity.ok(loanService.findAllPageable(pageable));
    }

    @Operation(
            description = "Create a new loan.",
            summary = "Create a new loan.",
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
    public ResponseEntity<LoanResponseDTO> insertLoan(@Validated(OnCreate.class) @RequestBody LoanRequestDTO loanRequestDTO) {
        System.out.println(loanRequestDTO.toString());
        LoanResponseDTO loanResponseDTO = loanService.insert(loanRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(loanResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(loanResponseDTO);
    }

    @Operation(
            description = "Update a loan.",
            summary = "Update a loan.",
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
    public ResponseEntity<LoanResponseDTO> updateLoan(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody LoanRequestDTO loanRequestDTO) {
        LoanResponseDTO loanResponseDTO = loanService.update(id, loanRequestDTO);
        return ResponseEntity.ok(loanResponseDTO);
    }

    @Operation(
            description = "Delete a loan.",
            summary = "Delete a loan if it is possible.",
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
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

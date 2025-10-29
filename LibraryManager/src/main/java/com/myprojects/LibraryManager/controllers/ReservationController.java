package com.myprojects.LibraryManager.controllers;

import com.myprojects.LibraryManager.dto.OnCreate;
import com.myprojects.LibraryManager.dto.OnUpdate;
import com.myprojects.LibraryManager.dto.reservation.ReservationRequestDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationResponseDTO;
import com.myprojects.LibraryManager.services.ReservationService;
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
@RequestMapping(value = "/reservations")
@Tag(name = "Reservations", description = "Controller for Reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Operation(
            description = "Get reservation by id.",
            summary = "Get reservation by id.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @Operation(
            description = "Find all pageable reservations.",
            summary = "Find all pageable reservations.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ReservationResponseDTO>> findAllReservations(Pageable pageable) {
        return ResponseEntity.ok(reservationService.findAll(pageable));
    }

    @Operation(
            description = "Find all reservations.",
            summary = "Find all reservations.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(value = "/all", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> findAllReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @Operation(
            description = "Find all reservations.",
            summary = "Find all reservations.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
            }
    )
    @GetMapping(value = "/confirmed", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> findAllConfirmed() {
        return ResponseEntity.ok(reservationService.findAllConfirmed());
    }

    @Operation(
            description = "Get overdue Reservations.",
            summary = "Get overdue Reservations.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/expired", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> getExpiredReservations() {
        return ResponseEntity.ok(reservationService.findExpiredReservations());
    }

    @Operation(
            description = "Get completed Reservations.",
            summary = "Get completed Reservations.",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
            }
    )
    @GetMapping(value = "/completed", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationResponseDTO>> getCompletedReservations() {
        return ResponseEntity.ok(reservationService.findCompletedReservations());
    }

    @Operation(
            description = "Create a new reservation.",
            summary = "Create a new reservation.",
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
    public ResponseEntity<ReservationResponseDTO> insertReservation(@Validated(OnCreate.class) @RequestBody ReservationRequestDTO reservationRequestDTO) {
        System.out.println(reservationRequestDTO.toString());
        ReservationResponseDTO reservationResponseDTO = reservationService.insert(reservationRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(reservationResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(reservationResponseDTO);
    }

    @Operation(
            description = "Update a reservation.",
            summary = "Update a reservation.",
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
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO reservationResponseDTO = reservationService.update(id, reservationRequestDTO);
        return ResponseEntity.ok(reservationResponseDTO);
    }

    @Operation(
            description = "Delete a reservation.",
            summary = "Delete a reservation if it is possible.",
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
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

package com.myprojects.LibraryManager.dto.user;

import com.myprojects.LibraryManager.dto.loan.LoanSimpleDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationSimpleDTO;
import com.myprojects.LibraryManager.entities.Loan;
import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private Set<LoanSimpleDTO> loans = new HashSet<LoanSimpleDTO>();
    private Set<ReservationSimpleDTO> reservations = new HashSet<ReservationSimpleDTO>();

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        for (Loan loan : user.getLoans()) {
            loans.add(new LoanSimpleDTO(loan));
        }
        for (Reservation reservation : user.getReservations()) {
            reservations.add(new ReservationSimpleDTO(reservation));
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

    public Set<LoanSimpleDTO> getLoans() {
        return loans;
    }

    public Set<ReservationSimpleDTO> getReservations() {
        return reservations;
    }
}

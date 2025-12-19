package com.myprojects.LibraryManager.dto.reservation;


import com.myprojects.LibraryManager.dto.book.BookSimpleDTO;
import com.myprojects.LibraryManager.dto.user.UserSimpleDTO;
import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;

import java.time.LocalDate;

public class ReservationResponseDTO {
    private Long id;
    private LocalDate reservationDate;
    private ReservationStatus status;
    private UserSimpleDTO user;
    private BookSimpleDTO book;

    public ReservationResponseDTO() {

    }

    public ReservationResponseDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationDate = reservation.getReservationDate();
        this.status = reservation.getStatus();
        this.user = new UserSimpleDTO(reservation.getUser());
        this.book = new BookSimpleDTO(reservation.getBook());
    }

    public Long getId() {
        return id;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public UserSimpleDTO getUser() {
        return user;
    }

    public BookSimpleDTO getBook() {
        return book;
    }
}

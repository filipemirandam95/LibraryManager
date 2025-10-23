package com.myprojects.LibraryManager.dto.reservation;

import com.myprojects.LibraryManager.dto.book.BookSimpleDTO;
import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;

import java.time.LocalDate;

public class ReservationSimpleDTO {
    private Long id;
    private LocalDate reservationDate;
    private ReservationStatus status;
    private BookSimpleDTO book;

    public ReservationSimpleDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationDate = reservation.getReservationDate();
        this.status = reservation.getStatus();
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

    public BookSimpleDTO getBookSimpleDTO() {
        return book;
    }
}

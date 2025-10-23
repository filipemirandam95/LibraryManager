package com.myprojects.LibraryManager.dto.reservation;

import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;

import java.time.LocalDate;

public class ReservationRequestDTO {

    private Long id;
    private LocalDate reservationDate;
    private ReservationStatus status;
    private Long userId;
    private Long bookId;

    public ReservationRequestDTO() {
    }

    public ReservationRequestDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationDate = reservation.getReservationDate();
        this.status = null;
        this.userId = reservation.getUser().getId();
        this.bookId = reservation.getBook().getId();
    }

    public ReservationRequestDTO(long id, LocalDate reservationDate, long userId, long bookId) {
        this.id = id;
        this.reservationDate = reservationDate;
        this.status = null;
        this.userId = userId;
        this.bookId = bookId;
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

    public Long getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }
}

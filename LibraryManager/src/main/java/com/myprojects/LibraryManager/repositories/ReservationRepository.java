package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @EntityGraph(attributePaths = {"user", "book"})
    Page<Reservation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "book"})
    List<Reservation> findAll();

    Optional<Reservation> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, ReservationStatus reservationStatus);
    List<Reservation> findByStatusAndReservationDateBefore(ReservationStatus status, LocalDate limite);

    @EntityGraph(attributePaths = {"user", "book"})
    @Query(value = "SELECT r FROM Reservation r WHERE r.reservationDate >= :dateThreshold AND r.status = :status ")
    List<Reservation> findAllConfirmedReservations(@Param("dateThreshold") LocalDate dateThreshold, @Param("status") ReservationStatus status);

    @EntityGraph(attributePaths = {"user", "book"})
    @Query(value = "SELECT r FROM Reservation r WHERE r.reservationDate < :dateThreshold AND r.status = :status")
    List<Reservation> findExpiredReservations(@Param("dateThreshold") LocalDate dateThreshold, @Param("status") ReservationStatus status);

    @EntityGraph(attributePaths = {"user", "book"})
    @Query(value = "SELECT r FROM Reservation r WHERE r.status = :status")
    List<Reservation> findCompletedReservations(@Param("status") ReservationStatus status);
}

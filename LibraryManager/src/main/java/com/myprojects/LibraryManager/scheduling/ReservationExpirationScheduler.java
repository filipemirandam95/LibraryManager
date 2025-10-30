package com.myprojects.LibraryManager.scheduling;

import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;
import com.myprojects.LibraryManager.repositories.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReservationExpirationScheduler {

    @Autowired
    private ReservationRepository reservationRepository;

    @Scheduled(fixedRate = 3600000) // a cada 1h
    @Transactional
    public void expireReservations() {
        LocalDate limit = LocalDate.now().minusDays(3);
        System.out.println("Scheduler method executed at " + LocalDate.now() + ", checking for expired reservations before " + limit);
        List<Reservation> expired = reservationRepository
                .findByStatusAndReservationDateBefore(ReservationStatus.CONFIRMED, limit);

        for (Reservation r : expired) {
            r.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(r);
        }
    }
}

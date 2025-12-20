package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.reservation.ReservationRequestDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationResponseDTO;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;
import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.BookRepository;
import com.myprojects.LibraryManager.repositories.ReservationRepository;
import com.myprojects.LibraryManager.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        authService.validateSelfOrAdmin(reservation.getUser().getId());
        return new ReservationResponseDTO(reservation);
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponseDTO> findAll(Pageable pageable) {
        Page<Reservation> result = reservationRepository.findAll(pageable);
        return result.map(x -> new ReservationResponseDTO(x));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        List<Reservation> reservationsList = reservationRepository.findAll();
        return reservationsList.stream().map(ReservationResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAllConfirmed() {
        LocalDate dateThreshold = LocalDate.now().minusDays(3);
        List<Reservation> reservationsList = reservationRepository.findAllConfirmedReservations(dateThreshold, ReservationStatus.CONFIRMED);
        return reservationsList.stream().map(ReservationResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findExpiredReservations() {
        LocalDate reservationThreshold = LocalDate.now().minusDays(3);
        List<Reservation> reservationsList = reservationRepository.findExpiredReservations(reservationThreshold, ReservationStatus.EXPIRED);
        return reservationsList.stream().map(ReservationResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findCompletedReservations() {
        List<Reservation> reservationsList = reservationRepository.findCompletedReservations(ReservationStatus.COMPLETED);
        return reservationsList.stream().map(ReservationResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDTO insert(ReservationRequestDTO reservationRequestDTO) {

        Optional<Reservation> r = reservationRepository
                .findByUserIdAndBookIdAndStatus(reservationRequestDTO.getUserId(), reservationRequestDTO.getBookId(), ReservationStatus.CONFIRMED);
        if (r.isPresent()) {
            System.out.println("r id: " + r.get().getId());
            throw new IllegalArgumentException("There already is a confirmed reservation for this user and book");
        }
        User user = userRepository.findById(reservationRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        Book book = bookRepository.findById(reservationRequestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        Reservation reservation = new Reservation();
        DTOtoModel(reservationRequestDTO, reservation);
        reservation.setUser(user);
        reservation.setBook(book);
        if (book.getAvailableCopies() > 0) {
            reservation = reservationRepository.save(reservation);
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);
        } else {
            throw new DatabaseException("There are no copies available at the moment.");
        }
        return new ReservationResponseDTO(reservation);
    }

    @Transactional
    public ReservationResponseDTO update(Long id, ReservationRequestDTO reservationRequestDTO) {
        try {
            Reservation reservation = reservationRepository.getReferenceById(id);
            DTOtoModel(reservationRequestDTO, reservation);
            reservation = reservationRepository.save(reservation);
            return new ReservationResponseDTO(reservation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            reservationRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");

        }
    }

    private void DTOtoModel(ReservationRequestDTO reservationRequestDTO, Reservation reservation) {
        if (reservationRequestDTO.getReservationDate() != null) { //If the field is null it means that this field will not be changed.
            reservation.setReservationDate((reservationRequestDTO.getReservationDate()));
        }
        if (reservationRequestDTO.getStatus() != null) {
            reservation.setStatus(reservationRequestDTO.getStatus());
        }
    }
}

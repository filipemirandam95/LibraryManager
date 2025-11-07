package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.reservation.ReservationRequestDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationResponseDTO;
import com.myprojects.LibraryManager.entities.Book;
import com.myprojects.LibraryManager.entities.Reservation;
import com.myprojects.LibraryManager.entities.ReservationStatus;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ForbiddenException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.BookRepository;
import com.myprojects.LibraryManager.repositories.ReservationRepository;
import com.myprojects.LibraryManager.repositories.UserRepository;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ReservationServiceTests {

    @InjectMocks
    ReservationService reservationService;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    AuthService authService;

    private Reservation operatorReservation;
    private Reservation validReservation;
    private ReservationRequestDTO validReservationRequestDTO;

    @BeforeEach
    void setUp() {
        validReservationRequestDTO = Factory.createReservationRequestDTO();
        operatorReservation = Factory.createReservationWithUserOperator();
        validReservation = Factory.createReservationWithUserOperator();

        when(reservationRepository.findById(operatorReservation.getId())).thenReturn(Optional.of(operatorReservation));
        when(reservationRepository
                .findByUserIdAndBookIdAndStatus(validReservationRequestDTO.getUserId(),
                        validReservationRequestDTO.getBookId(), ReservationStatus.CONFIRMED))
                .thenReturn(Optional.empty());
        when(userRepository.findById(validReservationRequestDTO.getUserId())).thenReturn(Optional.of(Factory.createUserOperator()));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(operatorReservation);


    }

    @Test
    void findByIdShouldReturnReservationResponseDTOWhenUserIsOperatorAndOwnId() {
        doNothing().when(authService).validateSelfOrAdmin(operatorReservation.getUser().getId());

        ReservationResponseDTO reservationResponseDTO = reservationService.findById(operatorReservation.getUser().getId());

        assertNotNull(reservationResponseDTO);
        verify(reservationRepository).findById(operatorReservation.getUser().getId());
    }

    @Test
    void findByIdShouldThrowForbiddenExceptionWhenUserIsOperatorAndDoesNotOwnId() {
        long notOwnerReservationId = 15L;
        Reservation notOwnerOperatorReservation = Factory.createReservationWithUserOperator(notOwnerReservationId);

        when(reservationRepository.findById(notOwnerReservationId)).thenReturn(Optional.of(notOwnerOperatorReservation));
        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(notOwnerOperatorReservation.getUser().getId());

        assertThrows(ForbiddenException.class, () -> {
            reservationService.findById(notOwnerReservationId);
        });
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        long invalidReservationId = 600L;
        doThrow(ResourceNotFoundException.class).when(reservationRepository).findById(invalidReservationId);

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.findById(invalidReservationId);
        });
    }

    @Test
    void insertShouldReturnReservationResponseDTOWhenReservationDoesNotExistAndUserAndBookExistsAndCopiesAreAvailable() {

        Book bookWithAvailableCopies = Factory.createBookWithAvailableCopies();

        when(bookRepository.findById(validReservationRequestDTO.getBookId())).thenReturn(Optional.of(bookWithAvailableCopies));
        when(bookRepository.save(operatorReservation.getBook())).thenReturn(operatorReservation.getBook());

        ReservationResponseDTO reservationResponseDTO = reservationService.insert(validReservationRequestDTO);

        assertNotNull(reservationResponseDTO);
        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(validReservationRequestDTO.getUserId(),
                        validReservationRequestDTO.getBookId(), ReservationStatus.CONFIRMED);
        verify(userRepository).findById(validReservationRequestDTO.getUserId());
        verify(bookRepository).findById(validReservationRequestDTO.getBookId());
        verify(reservationRepository).save(any(Reservation.class));
        verify(bookRepository).save(bookWithAvailableCopies);

    }


    @Test
    void insertShouldThrowIllegalArgumentExceptionWhenReservationExistsAndUserAndBookExists() {
        ReservationRequestDTO invalidReservationRequestDTO = Factory.createReservationRequestDTO();
        doThrow(IllegalArgumentException.class).when(reservationRepository)
                .findByUserIdAndBookIdAndStatus(invalidReservationRequestDTO.getUserId(),
                        invalidReservationRequestDTO.getBookId(), ReservationStatus.CONFIRMED);

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.insert(invalidReservationRequestDTO);
        });

        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(invalidReservationRequestDTO.getUserId(),
                        invalidReservationRequestDTO.getBookId(), ReservationStatus.CONFIRMED);

    }

    @Test
    void insertShouldThrowResourceNotFoundExceptionWhenReservationDoesNotExistAndUserDoesNotExist() {
        ReservationRequestDTO reservationRequestDTOWithInvalidUser = Factory.createReservationRequestDTO();

        when(reservationRepository
                .findByUserIdAndBookIdAndStatus(reservationRequestDTOWithInvalidUser.getUserId(),
                        reservationRequestDTOWithInvalidUser.getBookId(), ReservationStatus.CONFIRMED))
                .thenReturn(Optional.empty());
        doThrow(ResourceNotFoundException.class).when(userRepository)
                .findById(reservationRequestDTOWithInvalidUser.getUserId());

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.insert(reservationRequestDTOWithInvalidUser);
        });

        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(reservationRequestDTOWithInvalidUser.getUserId(),
                        reservationRequestDTOWithInvalidUser.getBookId(), ReservationStatus.CONFIRMED);
        verify(userRepository).findById(reservationRequestDTOWithInvalidUser.getUserId());

    }

    @Test
    void insertShouldThrowResourceNotFoundExceptionWhenReservationDoesNotExistAndUserExistsAndBookDoesNotExist() {
        ReservationRequestDTO reservationRequestDTOWithInvalidBook = Factory.createReservationRequestDTO();

        when(reservationRepository
                .findByUserIdAndBookIdAndStatus(reservationRequestDTOWithInvalidBook.getUserId(),
                        reservationRequestDTOWithInvalidBook.getBookId(), ReservationStatus.CONFIRMED))
                .thenReturn(Optional.empty());
        when(userRepository.findById(reservationRequestDTOWithInvalidBook.getUserId()))
                .thenReturn(Optional.of(Factory.createUserOperator()));
        doThrow(ResourceNotFoundException.class).when(bookRepository)
                .findById(reservationRequestDTOWithInvalidBook.getBookId());

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.insert(reservationRequestDTOWithInvalidBook);
        });

        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(reservationRequestDTOWithInvalidBook.getUserId(),
                        reservationRequestDTOWithInvalidBook.getBookId(), ReservationStatus.CONFIRMED);
        verify(bookRepository).findById(reservationRequestDTOWithInvalidBook.getBookId());

    }

    @Test
    void insertShouldThrowDataBaseExceptionWhenReservationDoesNotExistsAndUserExistsAndBookExistsAndCopiesAreNotAvailable() {
        Book bookWithoutAvailableCopies = Factory.createBookWithoutAvailableCopies();
        when(bookRepository.findById(validReservationRequestDTO.getBookId())).thenReturn(Optional.of(bookWithoutAvailableCopies));

        assertThrows(DatabaseException.class, () -> {
            reservationService.insert(validReservationRequestDTO);
        });

        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(validReservationRequestDTO.getUserId(),
                        validReservationRequestDTO.getBookId(), ReservationStatus.CONFIRMED);
        verify(userRepository).findById(validReservationRequestDTO.getUserId());
        verify(bookRepository).findById(validReservationRequestDTO.getBookId());
    }

    @Test
    void updateShouldReturnReservationResponseDTOWhenReservationIsValid() {
        when(reservationRepository.getReferenceById(validReservationRequestDTO.getId())).thenReturn(validReservation);
        when(reservationRepository.save(validReservation)).thenReturn(validReservation);

        ReservationResponseDTO reservationResponseDTO = reservationService.update(validReservationRequestDTO.getId(), validReservationRequestDTO);

        assertNotNull(reservationResponseDTO);
        verify(reservationRepository).getReferenceById(validReservationRequestDTO.getId());
        verify(reservationRepository).save(validReservation);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenReservationIdDoesNotExist() {
        ReservationRequestDTO nonExistingReservationRequestDTO = Factory.createReservationRequestDTO();
        doThrow(ResourceNotFoundException.class).when(reservationRepository).getReferenceById(nonExistingReservationRequestDTO.getId());

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.update(nonExistingReservationRequestDTO.getId(), nonExistingReservationRequestDTO);
        });

        verify(reservationRepository).getReferenceById(nonExistingReservationRequestDTO.getId());

    }

    @Test
    void deleteShouldDoNothingWhenReservationIdExists() {
        Long existingReservationId = 10L;
        when(reservationRepository.existsById(existingReservationId)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(existingReservationId);

        Assertions.assertDoesNotThrow(() -> {
            reservationService.delete(existingReservationId);
        });

        verify(reservationRepository).existsById(existingReservationId);
        verify(reservationRepository).deleteById(existingReservationId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenReservationIdDoesNotExists() {
        Long nonExistingReservationId = 900L;

        when(reservationRepository.existsById(nonExistingReservationId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.delete(nonExistingReservationId);
        });

        verify(reservationRepository).existsById(nonExistingReservationId);

    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenReservationIdHasDependency() {
        Long dependentReservationId = 2L;
        when(reservationRepository.existsById(dependentReservationId)).thenReturn(true);
        doThrow(DatabaseException.class).when(reservationRepository).deleteById(dependentReservationId);

        assertThrows(DatabaseException.class, () -> {
            reservationService.delete(dependentReservationId);
        });

        verify(reservationRepository).existsById(dependentReservationId);
        verify(reservationRepository).deleteById(dependentReservationId);
    }

}

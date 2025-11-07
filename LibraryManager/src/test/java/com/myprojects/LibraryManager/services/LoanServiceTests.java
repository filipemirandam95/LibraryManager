// java
package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.loan.LoanRequestDTO;
import com.myprojects.LibraryManager.dto.loan.LoanResponseDTO;
import com.myprojects.LibraryManager.entities.*;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ForbiddenException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.BookRepository;
import com.myprojects.LibraryManager.repositories.LoanRepository;
import com.myprojects.LibraryManager.repositories.ReservationRepository;
import com.myprojects.LibraryManager.repositories.UserRepository;
import com.myprojects.LibraryManager.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class LoanServiceTests {

    @InjectMocks
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private AuthService authService;

    private Loan existingLoanWithUserOperator;
    private Loan validLoan;
    private LoanRequestDTO validLoanRequestDTO;
    private LoanRequestDTO invalidLoanRequestDTO;
    private User user;
    Book book;
    Book bookWithoutAvailableCopies;
    private Long invalidLoanId;

    @BeforeEach
    void setUp() {
        invalidLoanId = 900L;
        existingLoanWithUserOperator = Factory.createLoanWithUserOperator();
        user = Factory.createUser();
        book = Factory.createBookWithAvailableCopies();
        bookWithoutAvailableCopies = Factory.createBookWithoutAvailableCopies();
        validLoanRequestDTO = Factory.createLoanRequestDTO(user, book);
        invalidLoanRequestDTO = Factory.createLoanRequestDTO(invalidLoanId, user, book);
        validLoan = Factory.createLoanFromLoanRequestDTO(validLoanRequestDTO);


        when(loanRepository.findById(existingLoanWithUserOperator.getId())).thenReturn(Optional.of(existingLoanWithUserOperator));
    }

    @Test
    void findByIdShouldReturnLoanResponseDTOWhenLoanExistsAndUserIsOperatorAndOwnId() {
        doNothing().when(authService).validateSelfOrAdmin(existingLoanWithUserOperator.getUser().getId());

        LoanResponseDTO loanResponseDTO = loanService.findById(existingLoanWithUserOperator.getId());

        assertNotNull(loanResponseDTO);
        assertEquals(loanResponseDTO.getUser().getId(), existingLoanWithUserOperator.getUser().getId());

    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenLoanIdDoesNotExist() {
        Long invalidLoanId = 900L;
        doThrow(ResourceNotFoundException.class).when(loanRepository).findById(invalidLoanId);

        assertThrows(ResourceNotFoundException.class, () -> {
            loanService.findById(invalidLoanId);
        });

        verify(loanRepository).findById(invalidLoanId);
    }

    @Test
    void findByIdShouldThrowForbiddenExceptionWhenLoanExistsAndUserIsOperatorAndDoesNotOwnId() {
        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(existingLoanWithUserOperator.getUser().getId());

        assertThrows(ForbiddenException.class, () -> {
            loanService.findById(existingLoanWithUserOperator.getId());
        });

        verify(loanRepository).findById(existingLoanWithUserOperator.getId());

    }

    @Test
    void insertShouldReturnLoanResponseDTOWhenLoanDoesNotExistAndUserAndBookExistsAndCopiesAreAvailable() {
        Book bookWithAvailableCopies = Factory.createBookWithAvailableCopies();
        LoanRequestDTO validLoanRequestDTO = Factory.createLoanRequestDTO(user, bookWithAvailableCopies);

        when(userRepository.findById(validLoanRequestDTO.getUserId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(validLoanRequestDTO.getBookId())).thenReturn(Optional.of(bookWithAvailableCopies));
        when(loanRepository.save(any(Loan.class))).thenReturn(Factory.createLoanFromLoanRequestDTO(validLoanRequestDTO));
        when(bookRepository.save(any(Book.class))).thenReturn(any(Book.class));

        LoanResponseDTO loanResponseDTO = loanService.insert(validLoanRequestDTO);

        assertNotNull(loanResponseDTO);
        assertEquals(loanResponseDTO.getUser().getId(), validLoanRequestDTO.getUserId());
        assertEquals(loanResponseDTO.getBook().getId(), validLoanRequestDTO.getBookId());
        assertEquals(loanResponseDTO.getLoanDate(), validLoanRequestDTO.getLoanDate());

        verify(userRepository).findById(validLoanRequestDTO.getUserId());
        verify(bookRepository).findById(validLoanRequestDTO.getBookId());
        verify(loanRepository).save(any(Loan.class));
        verify(bookRepository).save(any(Book.class));

    }

    @Test
    void insertShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        User newUser = Factory.createUser();
        Book bookWithAvailableCopies = Factory.createBookWithAvailableCopies();
        LoanRequestDTO loanRequestDTOWithInvalidUser = Factory.createLoanRequestDTO(newUser, bookWithAvailableCopies);

        doThrow(EntityNotFoundException.class).when(userRepository).findById(loanRequestDTOWithInvalidUser.getUserId());


        assertThrows(EntityNotFoundException.class, () -> {
            loanService.insert(loanRequestDTOWithInvalidUser);
        });

        verify(userRepository).findById(newUser.getId());
    }

    @Test
    void insertShouldThrowEntityNotFoundExceptionWhenUserExistsAndBookDoesNotExist() {
        Book newBook = Factory.createBookWithAvailableCopies();
        LoanRequestDTO loanRequestDTOWithInvalidBook = Factory.createLoanRequestDTO(user, newBook);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doThrow(EntityNotFoundException.class).when(bookRepository).findById(loanRequestDTOWithInvalidBook.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            loanService.insert(loanRequestDTOWithInvalidBook);
        });

        verify(userRepository).findById(user.getId());
        verify(bookRepository).findById(newBook.getId());

    }

    @Test
    void insertShouldReturnLoanResponseDTOWhenUserExistsAndBookExistsAndCopiesAreNotAvailableAndUserOwnReservation() {
        User userOwnerOfReservation = Factory.createUser();
        LoanRequestDTO loanRequestDTO = Factory.createLoanRequestDTO(userOwnerOfReservation, bookWithoutAvailableCopies);
        Reservation reservation = Factory
                .createReservation(1L, loanRequestDTO.getUserId(), loanRequestDTO.getBookId(), loanRequestDTO.getLoanDate(), ReservationStatus.CONFIRMED);

        when(userRepository.findById(loanRequestDTO.getUserId())).thenReturn(Optional.of(userOwnerOfReservation));
        when(bookRepository.findById(loanRequestDTO.getBookId())).thenReturn(Optional.of(bookWithoutAvailableCopies));
        when(reservationRepository
                .findByUserIdAndBookIdAndStatus(loanRequestDTO.getUserId(), loanRequestDTO.getBookId(), ReservationStatus.CONFIRMED))
                .thenReturn(Optional.of(reservation));

        when(loanRepository.save(any(Loan.class))).thenReturn(Factory.createLoanFromLoanRequestDTO(loanRequestDTO));
        when(bookRepository.save(any(Book.class))).thenReturn(any(Book.class));

        LoanResponseDTO loanResponseDTO = loanService.insert(loanRequestDTO);

        assertNotNull(loanResponseDTO);

        verify(userRepository).findById(loanRequestDTO.getUserId());
        verify(bookRepository).findById(loanRequestDTO.getBookId());
        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(loanRequestDTO.getUserId(), loanRequestDTO.getBookId(), ReservationStatus.CONFIRMED);
        verify(loanRepository).save(any(Loan.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void insertShouldThrowIllegalArgumentExceptionWhenUserExistsAndBookExistsAndCopiesAreNotAvailableAndUserDoesNotOwnReservation() {
        User userNotOwnerOfReservation = Factory.createUser();
        LoanRequestDTO loanRequestDTO = Factory.createLoanRequestDTO(userNotOwnerOfReservation, bookWithoutAvailableCopies);

        when(userRepository.findById(loanRequestDTO.getUserId())).thenReturn(Optional.of(userNotOwnerOfReservation));
        when(bookRepository.findById(loanRequestDTO.getBookId())).thenReturn(Optional.of(bookWithoutAvailableCopies));
        doThrow(IllegalArgumentException.class)
                .when(reservationRepository)
                .findByUserIdAndBookIdAndStatus(loanRequestDTO.getUserId(), loanRequestDTO.getBookId(), ReservationStatus.CONFIRMED);

        assertThrows(IllegalArgumentException.class, () -> {
            loanService.insert(loanRequestDTO);
        });

        verify(userRepository).findById(loanRequestDTO.getUserId());
        verify(bookRepository).findById(loanRequestDTO.getBookId());
        verify(reservationRepository)
                .findByUserIdAndBookIdAndStatus(loanRequestDTO.getUserId(), loanRequestDTO.getBookId(), ReservationStatus.CONFIRMED);

    }

    @Test
    void updateShouldReturnLoanResponseDTOWhenLoanIdExists() {

        when(loanRepository.getReferenceById(validLoan.getId())).thenReturn(validLoan);
        when(loanRepository.save(validLoan)).thenReturn(validLoan);

        LoanResponseDTO loanResponseDTO = loanService.update(validLoanRequestDTO.getId(), validLoanRequestDTO);

        assertNotNull(loanResponseDTO);
        assertEquals(loanResponseDTO.getId(), validLoanRequestDTO.getId());

        verify(loanRepository).getReferenceById(validLoan.getId());
        verify(loanRepository).save(validLoan);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenLoanIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(loanRepository).getReferenceById(invalidLoanId);

        assertThrows(ResourceNotFoundException.class, () -> {
            loanService.update(invalidLoanId, invalidLoanRequestDTO);
        });

        verify(loanRepository).getReferenceById(invalidLoanId);
    }

    @Test
    void deleteShouldDoNothingWhenLoanIdExists() {
        when(loanRepository.existsById(validLoanRequestDTO.getId())).thenReturn(true);
        doNothing().when(loanRepository).deleteById(validLoanRequestDTO.getId());

        assertDoesNotThrow(() -> {
            loanService.delete(validLoanRequestDTO.getId());
        });
        verify(loanRepository).existsById(validLoanRequestDTO.getId());
        verify(loanRepository).deleteById(validLoanRequestDTO.getId());

    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenLoanIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(loanRepository).existsById(invalidLoanId);

        assertThrows(ResourceNotFoundException.class, () -> {
            loanService.delete(invalidLoanId);
        });

        verify(loanRepository).existsById(invalidLoanId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenLoanHasDependency() {
        Long dependentId = 1L;

        when(loanRepository.existsById(dependentId)).thenReturn(true);
        doThrow(DatabaseException.class).when(loanRepository).deleteById(dependentId);

        assertThrows(DatabaseException.class, () -> {
            loanService.delete(dependentId);
        });

        verify(loanRepository).existsById(dependentId);
        verify(loanRepository).deleteById(dependentId);
    }
}

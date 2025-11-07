package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.fine.FineRequestDTO;
import com.myprojects.LibraryManager.dto.fine.FineResponseDTO;
import com.myprojects.LibraryManager.entities.Fine;
import com.myprojects.LibraryManager.entities.Loan;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.FineRepository;
import com.myprojects.LibraryManager.repositories.LoanRepository;
import com.myprojects.LibraryManager.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class FineServiceTests {

    @InjectMocks
    private FineService fineService;
    @Mock
    private FineRepository fineRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private AuthService authService;

    private Long validFineId;
    private Long invalidLoanId;
    private Long invalidFineId;
    private Long dependentFineId;
    private Fine validFine;
    private FineRequestDTO validFineRequestDTO;
    private FineRequestDTO invalidFineRequestDTO;
    private Loan loan;

    @BeforeEach
    void setUp() {
        validFineId = 1L;
        invalidFineId = 900L;
        invalidLoanId = 900L;
        dependentFineId = 10L;
        loan = Factory.createLoanWithUserOperator();
        validFine = Factory.createFine(validFineId, loan);
        validFineRequestDTO = Factory.createFineRequestDTO(validFine);
        invalidFineRequestDTO = Factory.createFineRequestDTO(invalidLoanId);


    }

    @Test
    void findByIdShouldReturnFineResponseDTOWhenFineIdExistsAndUserIsOperatorAndOwnFine() {
        when(fineRepository.findById(validFineId)).thenReturn(Optional.of(validFine));
        doNothing().when(authService).validateSelfOrAdmin(validFine.getLoan().getUser().getId());

        FineResponseDTO fineResponseDTO = fineService.findById(validFineId);

        assertNotNull(fineResponseDTO);
        assertEquals(fineResponseDTO.getId(), validFineId);

        verify(fineRepository).findById(validFineId);
        verify(authService).validateSelfOrAdmin(validFine.getLoan().getUser().getId());

    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenFineIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(fineRepository).findById(invalidFineId);

        assertThrows(ResourceNotFoundException.class, () -> {
                    fineService.findById(invalidFineId);
                }
        );

        verify(fineRepository).findById(invalidFineId);
    }

    @Test
    void insertShouldReturnFineResponseDTOWhenLoanExists() {
        when(loanRepository.findById(validFineRequestDTO.getLoanId())).thenReturn(Optional.of(loan));
        when(fineRepository.save(argThat(f ->
                f != null && Objects.equals(f.getLoan().getId(), validFineRequestDTO.getLoanId())
        ))).thenReturn(validFine);

        FineResponseDTO fineResponseDTO = fineService.insert(validFineRequestDTO);

        assertNotNull(fineResponseDTO);

        verify(loanRepository).findById(validFineRequestDTO.getLoanId());
        verify(fineRepository).save(argThat(f ->
                f != null && Objects.equals(f.getLoan().getId(), validFineRequestDTO.getLoanId())
        ));

    }

    @Test
    void insertShouldThrowEntityNotFoundExceptionWhenLoanDoesNotExist() {
        doThrow(EntityNotFoundException.class).when(loanRepository).findById(invalidFineRequestDTO.getLoanId());

        assertThrows(EntityNotFoundException.class, () -> {
            fineService.insert(invalidFineRequestDTO);
        });

        verify(loanRepository).findById(invalidFineRequestDTO.getLoanId());
    }

    @Test
    void updateShouldReturnFineResponseDTOWhenFineRequestIsValid() {
        when(fineRepository.getReferenceById(validFine.getId())).thenReturn(validFine);
        when(fineRepository.save(any(Fine.class))).thenReturn(validFine);

        FineResponseDTO fineResponseDTO = fineService.update(validFineId, validFineRequestDTO);
        assertNotNull(fineResponseDTO);
        verify(fineRepository).getReferenceById(validFineId);
        verify(fineRepository).save(any(Fine.class));

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenFineIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(fineRepository).getReferenceById(invalidFineRequestDTO.getId());

        assertThrows(ResourceNotFoundException.class, () -> {
            fineService.update(invalidFineRequestDTO.getId(), invalidFineRequestDTO);
        });

        verify(fineRepository).getReferenceById(invalidFineRequestDTO.getId());
    }

    @Test
    void deleteShouldDoNothingWhenFineIdExists() {
        when(fineRepository.existsById(validFineId)).thenReturn(true);
        doNothing().when(fineRepository).deleteById(validFineId);

        assertDoesNotThrow(() -> {
            fineService.delete(validFineId);
        });

        verify(fineRepository).existsById(validFineId);
        verify(fineRepository).deleteById(validFineId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenFineIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(fineRepository).existsById(invalidFineId);

        assertThrows(ResourceNotFoundException.class, () -> {
            fineService.delete(invalidFineId);
        });
        verify(fineRepository).existsById(invalidFineId);
        verify(fineRepository, times(0)).deleteById(invalidFineId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenFineHasDependency() {
        when(fineRepository.existsById(dependentFineId)).thenReturn(true);
        doThrow(DatabaseException.class).when(fineRepository).deleteById(dependentFineId);

        assertThrows(DatabaseException.class, () -> {
            fineService.delete(dependentFineId);
        });
        verify(fineRepository).existsById(dependentFineId);
        verify(fineRepository).deleteById(dependentFineId);

    }

}

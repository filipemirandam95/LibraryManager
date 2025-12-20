package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.loan.LoanRequestDTO;
import com.myprojects.LibraryManager.dto.loan.LoanResponseDTO;
import com.myprojects.LibraryManager.entities.*;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FineRepository fineRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public LoanResponseDTO findById(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        authService.validateSelfOrAdmin(loan.getUser().getId());
        return new LoanResponseDTO(loan);
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDTO> findAll() {
        List<Loan> loansList = loanRepository.findAll();
        return loansList.stream().map(LoanResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<LoanResponseDTO> findAll(Pageable pageable) {
        Page<Loan> loansPage = loanRepository.findAll(pageable);
        return loansPage.map(LoanResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDTO> findAllOpened() {
        LocalDate dateThreshold = LocalDate.now().minusDays(7);
        List<Loan> loansList = loanRepository.findAllOpenedLoans(dateThreshold);
        return loansList.stream().map(LoanResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoanResponseDTO> findOverdueLoans() {
        LocalDate loanThreshold = LocalDate.now().minusDays(7);
        List<Loan> loansList = loanRepository.findOverdueLoans(loanThreshold);
        return loansList.stream().map(LoanResponseDTO::new).collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<LoanResponseDTO> findMostLoanedBooksInPeriod(LocalDate startDate, LocalDate endDate, int limit) {
//        Pageable pageable = PageRequest.of(0, limit);
//        List<Loan> loansList = loanRepository.findMostLoanedBooksInPeriod(startDate, endDate, pageable);
//        return loansList.stream().map(LoanResponseDTO::new).collect(Collectors.toList());
//    }

//    @Transactional(readOnly = true)
//    public List<LoanResponseDTO> findReturnedLoansWithDelay() {
//        LocalDate dateThreshold = LocalDate.now().minusDays(7);
//        List<Loan> loansList = loanRepository.findReturnedLoansWithDelay(dateThreshold);
//        return loansList.stream().map(LoanResponseDTO::new).collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Page<LoanResponseDTO> findAllPageable(Pageable pageable) {
        Page<Loan> result = loanRepository.findAll(pageable);
        return result.map(LoanResponseDTO::new);
    }

    @Transactional
    public LoanResponseDTO insert(LoanRequestDTO loanRequestDTO) {
        User user = userRepository.findById(loanRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(loanRequestDTO.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.getAvailableCopies() == 0) { // if there are no copies available it is necessary to check if the user has the reservation for it therefore isn't available for others.
            reservationRepository.findByUserIdAndBookIdAndStatus(user.getId(), book.getId(), ReservationStatus.CONFIRMED)
                    .orElseThrow(() -> new IllegalArgumentException("There are no copies available."));
        }
        Loan loan = new Loan();
        DTOtoModel(loanRequestDTO, loan);
        loan.setUser(user);
        loan.setBook(book);
        loan = loanRepository.save(loan);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        return new LoanResponseDTO(loan);

    }

    @Transactional
    public LoanResponseDTO update(Long id, LoanRequestDTO loanRequestDTO) {
        try {
            Loan loan = loanRepository.getReferenceById(id);
            DTOtoModel(loanRequestDTO, loan);
            if (loan.getReturnDate().isAfter(loan.getLoanDate().plusDays(7))) {
                long lateDays = ChronoUnit.DAYS.between(loan.getLoanDate().plusDays(7), loan.getReturnDate());
                BigDecimal amount = new BigDecimal(3).multiply(BigDecimal.valueOf(lateDays)); // $3 fee for every overdue day.
                Fine fine = new Fine(amount, Instant.now(), null, false, loan);
                loan.setFine(fine);
            }
            loan = loanRepository.save(loan);
            return new LoanResponseDTO(loan);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            loanRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");

        }
    }

    private void DTOtoModel(LoanRequestDTO loanRequestDTO, Loan loan) {
        if (loanRequestDTO.getLoanDate() != null) { //If the field is null it means that this field will not be changed.
            loan.setLoanDate(loanRequestDTO.getLoanDate());
        }
        if (loanRequestDTO.getReturnDate() != null) {
            loan.setReturnDate(loanRequestDTO.getReturnDate());
        }
    }
}

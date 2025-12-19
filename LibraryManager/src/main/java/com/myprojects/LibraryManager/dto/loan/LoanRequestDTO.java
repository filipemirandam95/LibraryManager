package com.myprojects.LibraryManager.dto.loan;

import com.myprojects.LibraryManager.entities.Loan;

import java.time.LocalDate;

public class LoanRequestDTO {

    private Long id;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Long userId;
    private Long bookId;

    public LoanRequestDTO() {
    }

    public LoanRequestDTO(Loan loan) {
        this.id = loan.getId();
        this.loanDate = loan.getLoanDate();
        this.returnDate = loan.getReturnDate();
        this.userId = loan.getUser().getId();
        this.bookId = loan.getBook().getId();
    }

    public LoanRequestDTO(Long id, LocalDate loanDate, LocalDate returnDate, Long userId, Long bookId) {
        this.id = id;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.userId = userId;
        this.bookId = bookId;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public long getUserId() {
        return userId;
    }

    public long getBookId() {
        return bookId;
    }
}

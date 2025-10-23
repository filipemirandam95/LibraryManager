package com.myprojects.LibraryManager.dto.loan;

import com.myprojects.LibraryManager.dto.book.BookDetailedDTO;
import com.myprojects.LibraryManager.dto.fine.FineSimpleDTO;
import com.myprojects.LibraryManager.entities.Loan;

import java.time.LocalDate;

public class LoanSimpleDTO {

    private Long id;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private BookDetailedDTO book;
    private FineSimpleDTO fine;

    public LoanSimpleDTO(Loan loan) {
        this.id = loan.getId();
        this.loanDate = loan.getLoanDate();
        this.returnDate = loan.getReturnDate();
        this.book = new BookDetailedDTO(loan.getBook());
        if (loan.getFine() != null) {
            this.fine = new FineSimpleDTO(loan.getFine());
        }
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

    public BookDetailedDTO getBook() {
        return book;
    }

    public FineSimpleDTO getFine() {
        return fine;
    }
}

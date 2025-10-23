package com.myprojects.LibraryManager.dto.loan;

import com.myprojects.LibraryManager.dto.book.BookSimpleDTO;
import com.myprojects.LibraryManager.dto.fine.FineSimpleDTO;
import com.myprojects.LibraryManager.dto.user.UserSimpleDTO;
import com.myprojects.LibraryManager.entities.Loan;

import java.time.LocalDate;

public class LoanResponseDTO {

    private Long id;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private UserSimpleDTO user;
    private BookSimpleDTO book;
    private FineSimpleDTO fine;

    public LoanResponseDTO() {
    }

    public LoanResponseDTO(Loan loan) {
        this.id = loan.getId();
        this.loanDate = loan.getLoanDate();
        this.returnDate = loan.getReturnDate();
        if(loan.getUser()!=null){
            this.user = new UserSimpleDTO(loan.getUser());
        }
        if(loan.getBook()!=null){
            this.book = new BookSimpleDTO(loan.getBook());
        }
        if(loan.getFine()!=null){
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

    public UserSimpleDTO getUser() {
        return user;
    }

    public BookSimpleDTO getBook() {
        return book;
    }

    public FineSimpleDTO getFine() {
        return fine;
    }

}

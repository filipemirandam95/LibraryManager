package com.myprojects.LibraryManager.entities;

import com.myprojects.LibraryManager.dto.loan.LoanRequestDTO;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate loanDate;
    private LocalDate returnDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @OneToOne(mappedBy = "loan", cascade = CascadeType.ALL)
    private Fine fine;

    public Loan() {

    }

    public Loan(Long id) {
        this.id = id;
    }

    public Loan(LoanRequestDTO loanRequestDTO) {
        this.id = loanRequestDTO.getId();
        this.loanDate = loanRequestDTO.getLoanDate();
        this.returnDate = loanRequestDTO.getReturnDate();
        this.user = new User(loanRequestDTO.getUserId());
        this.book = new Book(loanRequestDTO.getBookId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Fine getFine() {
        return fine;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
    }
}

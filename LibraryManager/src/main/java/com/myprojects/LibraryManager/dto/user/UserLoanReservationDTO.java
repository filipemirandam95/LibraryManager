package com.myprojects.LibraryManager.dto.user;

import java.time.LocalDate;

public class UserLoanReservationDTO {
    private Long userId;
    private String name;
    private String email;

    private Long loanId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Long bookLoanId;
    private String bookLoanTitle;

    private Long reservationId;
    private LocalDate reservationDate;
    private Long bookResId;
    private String bookResTitle;

    public UserLoanReservationDTO() {
    }

    public UserLoanReservationDTO(Long userId, String name, String email, Long loanId, LocalDate loanDate, LocalDate returnDate, Long bookLoanId, String bookLoanTitle, Long reservationId, LocalDate reservationDate, Long bookResId, String bookResTitle) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.loanId = loanId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.bookLoanId = bookLoanId;
        this.bookLoanTitle = bookLoanTitle;
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.bookResId = bookResId;
        this.bookResTitle = bookResTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
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

    public Long getBookLoanId() {
        return bookLoanId;
    }

    public void setBookLoanId(Long bookLoanId) {
        this.bookLoanId = bookLoanId;
    }

    public String getBookLoanTitle() {
        return bookLoanTitle;
    }

    public void setBookLoanTitle(String bookLoanTitle) {
        this.bookLoanTitle = bookLoanTitle;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Long getBookResId() {
        return bookResId;
    }

    public void setBookResId(Long bookResId) {
        this.bookResId = bookResId;
    }

    public String getBookResTitle() {
        return bookResTitle;
    }

    public void setBookResTitle(String bookResTitle) {
        this.bookResTitle = bookResTitle;
    }
}

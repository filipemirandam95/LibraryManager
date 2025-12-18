package com.myprojects.LibraryManager.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_fine")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    BigDecimal amount;
    private Instant appliedAt;
    private Instant paidAt;
    Boolean paid;
    @OneToOne
    private Loan loan;

    public Fine() {

    }
    public Fine(Long id) {
        this.id = id;
    }

    public Fine(BigDecimal amount, Instant appliedAt, Instant paidAt, Boolean paid, Loan loan) {
        this.amount = amount;
        this.appliedAt = appliedAt;
        this.paidAt = paidAt;
        this.paid = paid;
        this.loan = loan;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}

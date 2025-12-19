package com.myprojects.LibraryManager.dto.fine;

import com.myprojects.LibraryManager.entities.Fine;

import java.math.BigDecimal;
import java.time.Instant;


public class FineRequestDTO {

    private long id;
    BigDecimal amount;
    private Instant appliedAt;
    private Instant paidAt;
    boolean paid;
    private Long loanId;

    public FineRequestDTO() {
    }

    public FineRequestDTO(Long loanId) {
        this.loanId = loanId;
    }

    public FineRequestDTO(Fine fine) {
        this.id = fine.getId();
        this.amount = fine.getAmount();
        this.appliedAt = fine.getAppliedAt();
        this.paidAt = fine.getPaidAt();
        this.paid = fine.isPaid();
        this.loanId = fine.getLoan().getId();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Boolean isPaid() {
        return paid;
    }

    public Long getLoanId() {
        return loanId;
    }
}

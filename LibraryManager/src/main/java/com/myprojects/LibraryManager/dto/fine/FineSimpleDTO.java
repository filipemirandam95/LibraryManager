package com.myprojects.LibraryManager.dto.fine;

import com.myprojects.LibraryManager.entities.Fine;

import java.math.BigDecimal;
import java.time.Instant;

public class FineSimpleDTO {
    private long id;
    BigDecimal amount;
    private Instant appliedAt;
    private Instant paidAt;
    boolean paid;

    public FineSimpleDTO(Fine fine) {
        this.id = fine.getId();
        this.amount = fine.getAmount();
        this.appliedAt = fine.getAppliedAt();
        this.paidAt = fine.getPaidAt();
        this.paid = fine.isPaid();
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

    public boolean isPaid() {
        return paid;
    }
}

package com.syed.finance_tracker.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    public IncomeResponse(
            Long id,
            BigDecimal amount,
            String description,
            LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
}
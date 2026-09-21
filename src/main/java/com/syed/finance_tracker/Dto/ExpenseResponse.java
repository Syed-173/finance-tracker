package com.syed.finance_tracker.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    private Long categoryId;
    private String categoryName;

    public ExpenseResponse(
            Long id,
            BigDecimal amount,
            String description,
            LocalDate date,
            Long categoryId,
            String categoryName) {

        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}

package com.syed.finance_tracker.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetResponse {

    private Long id;
    private BigDecimal amount;
    private LocalDate month;
    private Long categoryId;
    private String categoryName;

    public BudgetResponse(
            Long id,
            BigDecimal amount,
            LocalDate month,
            Long categoryId,
            String categoryName) {

        this.id = id;
        this.amount = amount;
        this.month = month;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getMonth() {
        return month;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}

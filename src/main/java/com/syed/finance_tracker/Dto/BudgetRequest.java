package com.syed.finance_tracker.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetRequest {

    private BigDecimal amount;

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private LocalDate month;
    private Long categoryId;

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getMonth() {
        return month;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}

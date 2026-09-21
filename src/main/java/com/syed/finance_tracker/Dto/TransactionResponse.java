package com.syed.finance_tracker.Dto;

import com.syed.finance_tracker.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
    private Long categoryId;
    private String categoryName;

    public TransactionResponse(
            Long id,
            BigDecimal amount,
            String description,
            LocalDate date,
            TransactionType type,
            Long categoryId,
            String categoryName) {

        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
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

    public TransactionType getType() {
        return type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
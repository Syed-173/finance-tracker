package com.syed.finance_tracker.Dto;

import java.math.BigDecimal;

public class BudgetAlertResponse {
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getPercentageUsed() {
        return percentageUsed;
    }

    public void setPercentageUsed(BigDecimal percentageUsed) {
        this.percentageUsed = percentageUsed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private BigDecimal budgetAmount;

    public BudgetAlertResponse(String categoryName, BigDecimal budgetAmount, BigDecimal spentAmount, BigDecimal percentageUsed, String message) {
        this.categoryName = categoryName;
        this.budgetAmount = budgetAmount;
        this.spentAmount = spentAmount;
        this.percentageUsed = percentageUsed;
        this.message = message;
    }

    private BigDecimal spentAmount;
    private BigDecimal percentageUsed;
    private String message;
}

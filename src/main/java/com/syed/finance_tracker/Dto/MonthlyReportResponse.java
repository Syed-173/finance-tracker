package com.syed.finance_tracker.Dto;

import java.math.BigDecimal;
import java.util.Map;

public class MonthlyReportResponse {

    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal remaining;

    private Map<String, BigDecimal> expensesByCategory;

    public MonthlyReportResponse(
            String month,
            BigDecimal totalIncome,
            BigDecimal totalExpenses,
            BigDecimal remaining,
            Map<String, BigDecimal> expensesByCategory) {

        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.remaining = remaining;
        this.expensesByCategory = expensesByCategory;
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public Map<String, BigDecimal> getExpensesByCategory() {
        return expensesByCategory;
    }

    public void setExpensesByCategory(Map<String, BigDecimal> expensesByCategory) {
        this.expensesByCategory = expensesByCategory;
    }
}

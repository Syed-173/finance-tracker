package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.ExpenseRequest;
import com.syed.finance_tracker.Dto.ExpenseResponse;
import com.syed.finance_tracker.Service.ExpenseService;
import com.syed.finance_tracker.entity.Expense;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/expenses")
    public String addExpense(@RequestBody ExpenseRequest expenseRequest){
        return expenseService.createExpense(expenseRequest);
    }

    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpensesByUser(){
        return expenseService.getExpenses();
    }

    @GetMapping("/expenses/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable Long expenseId) {
        return expenseService.getExpenseById(expenseId);
    }

    @PutMapping("/expenses/{expenseId}")
    public ExpenseResponse updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseRequest expenseRequest) {

        return expenseService.updateExpense(expenseId, expenseRequest);
    }

    @DeleteMapping("/expenses/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {
        return expenseService.deleteExpense(expenseId);
    }

}

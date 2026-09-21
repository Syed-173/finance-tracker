package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.BudgetRequest;
import com.syed.finance_tracker.Dto.BudgetResponse;
import com.syed.finance_tracker.Service.BudgetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/budgets")
    public String addBudget(
            @RequestBody BudgetRequest budgetRequest) {

        return budgetService.createBudget(budgetRequest);
    }

    @GetMapping("/budgets")
    public List<BudgetResponse> getBudgets() {

        return budgetService.getBudgets();
    }

    @GetMapping("/budgets/{budgetId}")
    public BudgetResponse getBudgetById(
            @PathVariable Long budgetId) {

        return budgetService.getBudgetById(budgetId);
    }

    @PutMapping("/budgets/{budgetId}")
    public BudgetResponse updateBudget(
            @PathVariable Long budgetId,
            @RequestBody BudgetRequest budgetRequest) {

        return budgetService.updateBudget(
                budgetId,
                budgetRequest
        );
    }

    @DeleteMapping("/budgets/{budgetId}")
    public String deleteBudget(
            @PathVariable Long budgetId) {

        return budgetService.deleteBudget(budgetId);
    }
}
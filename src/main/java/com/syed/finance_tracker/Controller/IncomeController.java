package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.IncomeRequest;
import com.syed.finance_tracker.Dto.IncomeResponse;
import com.syed.finance_tracker.Service.IncomeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/income")
    public String addIncome(@RequestBody IncomeRequest incomeRequest) {
        return incomeService.createIncome(incomeRequest);
    }

    @GetMapping("/income")
    public List<IncomeResponse> getIncomes() {
        return incomeService.getIncomes();
    }

    @GetMapping("/income/{incomeId}")
    public IncomeResponse getIncomeById(
            @PathVariable Long incomeId) {

        return incomeService.getIncomeById(incomeId);
    }

    @PutMapping("/income/{incomeId}")
    public IncomeResponse updateIncome(
            @PathVariable Long incomeId,
            @RequestBody IncomeRequest incomeRequest) {

        return incomeService.updateIncome(incomeId, incomeRequest);
    }

    @DeleteMapping("/income/{incomeId}")
    public String deleteIncome(@PathVariable Long incomeId) {
        return incomeService.deleteIncome(incomeId);
    }
}
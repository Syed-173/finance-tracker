package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.TransactionResponse;
import com.syed.finance_tracker.Service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions() {

        return transactionService.getTransactions();
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionResponse getTransactionById(
            @PathVariable Long transactionId) {

        return transactionService
                .getTransactionById(transactionId);
    }
}
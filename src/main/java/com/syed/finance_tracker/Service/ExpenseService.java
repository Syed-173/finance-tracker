package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.ExpenseRequest;
import com.syed.finance_tracker.Dto.ExpenseResponse;
import com.syed.finance_tracker.Repository.CategoryRepository;
import com.syed.finance_tracker.Repository.ExpenseRepository;
import com.syed.finance_tracker.Repository.TransactionRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetAlertService budgetAlertService;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            BudgetAlertService budgetAlertService) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetAlertService = budgetAlertService;
    }

    @Transactional
    public String createExpense(ExpenseRequest expenseRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(
                        expenseRequest.getCategoryId(),
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = new Expense();

        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());
        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);

        Transaction transaction = new Transaction();

        transaction.setSourceId(savedExpense.getId());
        transaction.setAmount(savedExpense.getAmount());
        transaction.setDescription(savedExpense.getDescription());
        transaction.setDate(savedExpense.getDate());
        transaction.setType(TransactionType.EXPENSE);
        transaction.setUser(user);
        transaction.setCategory(category);

        transactionRepository.save(transaction);

        budgetAlertService.createBudgetNotification(savedExpense);

        return "expense added";
    }

    public List<ExpenseResponse> getExpenses() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Expense> expenses =
                expenseRepository.findByUser_Id(user.getId());

        return expenses.stream()
                .map(expense -> new ExpenseResponse(
                        expense.getId(),
                        expense.getAmount(),
                        expense.getDescription(),
                        expense.getDate(),
                        expense.getCategory().getId(),
                        expense.getCategory().getName()
                ))
                .toList();
    }

    public ExpenseResponse getExpenseById(Long expenseId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<Expense> expense =
                expenseRepository.findByIdAndUser_Id(expenseId, user.getId());

        Expense expenseResult = expense
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        return new ExpenseResponse(
                expenseResult.getId(),
                expenseResult.getAmount(),
                expenseResult.getDescription(),
                expenseResult.getDate(),
                expenseResult.getCategory().getId(),
                expenseResult.getCategory().getName()
        );
    }

    @Transactional
    public ExpenseResponse updateExpense(
            Long expenseId,
            ExpenseRequest expenseRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Expense expense = expenseRepository
                .findByIdAndUser_Id(expenseId, user.getId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(
                        expenseRequest.getCategoryId(),
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());
        expense.setCategory(category);

        Expense updatedExpense = expenseRepository.save(expense);

        Transaction transaction = transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        expenseId,
                        TransactionType.EXPENSE,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(updatedExpense.getAmount());
        transaction.setDescription(updatedExpense.getDescription());
        transaction.setDate(updatedExpense.getDate());
        transaction.setCategory(updatedExpense.getCategory());

        transactionRepository.save(transaction);

        budgetAlertService.createBudgetNotification(updatedExpense);

        return new ExpenseResponse(
                updatedExpense.getId(),
                updatedExpense.getAmount(),
                updatedExpense.getDescription(),
                updatedExpense.getDate(),
                updatedExpense.getCategory().getId(),
                updatedExpense.getCategory().getName()
        );
    }

    @Transactional
    public String deleteExpense(Long expenseId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Expense expense = expenseRepository
                .findByIdAndUser_Id(expenseId, user.getId())
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        Transaction transaction = transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        expenseId,
                        TransactionType.EXPENSE,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(transaction);

        expenseRepository.delete(expense);

//        budgetAlertService.createBudgetNotification(expense);

        return "expense deleted";
    }
}
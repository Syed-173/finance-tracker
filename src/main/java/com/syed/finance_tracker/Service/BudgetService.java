package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.BudgetRequest;
import com.syed.finance_tracker.Dto.BudgetResponse;
import com.syed.finance_tracker.Repository.BudgetRepository;
import com.syed.finance_tracker.Repository.CategoryRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.Budget;
import com.syed.finance_tracker.entity.Category;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository) {

        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public String createBudget(BudgetRequest budgetRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(
                        budgetRequest.getCategoryId(),
                        user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        Budget budget = new Budget();

        budget.setAmount(budgetRequest.getAmount());
        budget.setMonth(budgetRequest.getMonth());
        budget.setUser(user);
        budget.setCategory(category);

        budgetRepository.save(budget);

        return "budget added";
    }

    public List<BudgetResponse> getBudgets() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<Budget> budgets =
                budgetRepository.findByUser_Id(user.getId());

        return budgets.stream()
                .map(budget -> new BudgetResponse(
                        budget.getId(),
                        budget.getAmount(),
                        budget.getMonth(),
                        budget.getCategory().getId(),
                        budget.getCategory().getName()
                ))
                .toList();
    }

    public BudgetResponse getBudgetById(Long budgetId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Budget budget = budgetRepository
                .findByIdAndUser_Id(budgetId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Budget not found"));

        return new BudgetResponse(
                budget.getId(),
                budget.getAmount(),
                budget.getMonth(),
                budget.getCategory().getId(),
                budget.getCategory().getName()
        );
    }

    public BudgetResponse updateBudget(
            Long budgetId,
            BudgetRequest budgetRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Budget budget = budgetRepository
                .findByIdAndUser_Id(budgetId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Budget not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(
                        budgetRequest.getCategoryId(),
                        user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        budget.setAmount(budgetRequest.getAmount());
        budget.setMonth(budgetRequest.getMonth());
        budget.setCategory(category);

        Budget updatedBudget = budgetRepository.save(budget);

        return new BudgetResponse(
                updatedBudget.getId(),
                updatedBudget.getAmount(),
                updatedBudget.getMonth(),
                updatedBudget.getCategory().getId(),
                updatedBudget.getCategory().getName()
        );
    }

    public String deleteBudget(Long budgetId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Budget budget = budgetRepository
                .findByIdAndUser_Id(budgetId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Budget not found"));

        budgetRepository.delete(budget);

        return "budget deleted";
    }
}
package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.BudgetAlertResponse;
import com.syed.finance_tracker.Repository.BudgetRepository;
import com.syed.finance_tracker.Repository.ExpenseRepository;
import com.syed.finance_tracker.Repository.NotificationRepository;
import com.syed.finance_tracker.entity.Budget;
import com.syed.finance_tracker.entity.Expense;
import com.syed.finance_tracker.entity.Notification;
import com.syed.finance_tracker.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetAlertService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final NotificationRepository notificationRepository;

    public BudgetAlertService(
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository,
            NotificationRepository notificationRepository) {

        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<BudgetAlertResponse> getBudgetAlerts(User user) {

        List<Budget> budgets =
                budgetRepository.findByUser_Id(user.getId());

        List<BudgetAlertResponse> alerts = new ArrayList<>();

        for (Budget budget : budgets) {

            LocalDate startDate = budget.getMonth();

            LocalDate endDate = budget.getMonth()
                    .withDayOfMonth(
                            budget.getMonth().lengthOfMonth()
                    );

            List<Expense> expenses =
                    expenseRepository.findByUser_IdAndCategory_IdAndDateBetween(
                            user.getId(),
                            budget.getCategory().getId(),
                            startDate,
                            endDate
                    );

            BigDecimal spentAmount = expenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal budgetAmount = budget.getAmount();

            if (budgetAmount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal percentageUsed =
                    spentAmount
                            .multiply(BigDecimal.valueOf(100))
                            .divide(
                                    budgetAmount,
                                    2,
                                    RoundingMode.HALF_UP
                            );

            String message;

            if (spentAmount.compareTo(budgetAmount) > 0) {
                message = "Budget exceeded";
            } else if (percentageUsed.compareTo(BigDecimal.valueOf(90)) >= 0) {
                message = "Budget almost exceeded";
            } else {
                message = "Budget under control";
            }

            alerts.add(
                    new BudgetAlertResponse(
                            budget.getCategory().getName(),
                            budgetAmount,
                            spentAmount,
                            percentageUsed,
                            message
                    )
            );
        }

        return alerts;
    }


    @Transactional
    public void createBudgetNotification(Expense expense) {

        User user = expense.getUser();

        Long categoryId = expense.getCategory().getId();

        LocalDate month = expense.getDate()
                .withDayOfMonth(1);

        Optional<Budget> budgetOptional =
                budgetRepository.findByUser_IdAndCategory_IdAndMonth(
                        user.getId(),
                        categoryId,
                        month
                );

        if (budgetOptional.isEmpty()) {
            return;
        }

        Budget budget = budgetOptional.get();

        BigDecimal budgetAmount = budget.getAmount();

        if (budgetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        LocalDate startDate = budget.getMonth();

        LocalDate endDate = budget.getMonth()
                .withDayOfMonth(
                        budget.getMonth().lengthOfMonth()
                );

        List<Expense> expenses =
                expenseRepository.findByUser_IdAndCategory_IdAndDateBetween(
                        user.getId(),
                        categoryId,
                        startDate,
                        endDate
                );

        BigDecimal spentAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal percentageUsed =
                spentAmount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                budgetAmount,
                                2,
                                RoundingMode.HALF_UP
                        );

        String type;
        String message;

        if (spentAmount.compareTo(budgetAmount) > 0) {

            type = "BUDGET_EXCEEDED";

            message = "Your "
                    + budget.getCategory().getName()
                    + " budget has been exceeded.";

        } else if (percentageUsed.compareTo(BigDecimal.valueOf(90)) >= 0) {

            type = "BUDGET_WARNING";

            message = "You have used "
                    + percentageUsed
                    + "% of your "
                    + budget.getCategory().getName()
                    + " budget.";

        } else {
            return;
        }

        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setType(type);
        notification.setUser(user);
        notification.setCategory(budget.getCategory());
        notification.setMonth(budget.getMonth());

        notificationRepository.save(notification);
    }
}
package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.MonthlyReportResponse;
import com.syed.finance_tracker.Repository.ExpenseRepository;
import com.syed.finance_tracker.Repository.IncomeRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.Expense;
import com.syed.finance_tracker.entity.Income;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public ReportService(
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public MonthlyReportResponse getMonthlyReport(String month) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        YearMonth yearMonth = YearMonth.parse(month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Expense> expenses =
                expenseRepository.findByUser_IdAndDateBetween(
                        user.getId(),
                        startDate,
                        endDate);

        List<Income> incomes =
                incomeRepository.findByUser_IdAndDateBetween(
                        user.getId(),
                        startDate,
                        endDate);

        BigDecimal totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = incomes.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining =
                totalIncome.subtract(totalExpenses);

        Map<String, BigDecimal> expensesByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));

        return new MonthlyReportResponse(
                month,
                totalIncome,
                totalExpenses,
                remaining,
                expensesByCategory
        );
    }
}

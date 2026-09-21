package com.syed.finance_tracker.Repository;

import com.syed.finance_tracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser_Id(Long userId);

    Optional<Expense> findByIdAndUser_Id(Long expenseId, Long userId);

    List<Expense> findByUser_IdAndDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Expense> findByUser_IdAndCategory_IdAndDateBetween(
            Long userId,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate
    );
}

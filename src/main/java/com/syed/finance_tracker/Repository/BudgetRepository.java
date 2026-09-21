package com.syed.finance_tracker.Repository;

import com.syed.finance_tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser_Id(Long userId);

    Optional<Budget> findByIdAndUser_Id(Long budgetId, Long userId);

    Optional<Budget> findByUser_IdAndCategory_IdAndMonth(
            Long userId,
            Long categoryId,
            LocalDate month
    );
}

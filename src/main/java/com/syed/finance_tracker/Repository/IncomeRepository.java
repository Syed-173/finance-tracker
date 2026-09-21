package com.syed.finance_tracker.Repository;

import com.syed.finance_tracker.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser_Id(Long userId);

    Optional<Income> findByIdAndUser_Id(Long incomeId, Long userId);

    List<Income> findByUser_IdAndDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}

package com.syed.finance_tracker.Repository;

import com.syed.finance_tracker.entity.Transaction;
import com.syed.finance_tracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser_Id(Long userId);

    Optional<Transaction> findByIdAndUser_Id(
            Long transactionId,
            Long userId
    );

    Optional<Transaction> findByTypeAndSourceIdAndUser_Id(
            TransactionType type,
            Long sourceId,
            Long userId
    );

    Optional<Transaction> findBySourceIdAndTypeAndUser_Id(
            Long sourceId,
            TransactionType type,
            Long userId
    );
}

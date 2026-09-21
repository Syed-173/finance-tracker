package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.TransactionResponse;
import com.syed.finance_tracker.Repository.TransactionRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.Transaction;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> getTransactions() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return transactionRepository
                .findByUser_Id(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse getTransactionById(Long transactionId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Transaction transaction =
                transactionRepository
                        .findByIdAndUser_Id(
                                transactionId,
                                user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Transaction not found"));

        return toResponse(transaction);
    }

    private TransactionResponse toResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCategory() != null
                        ? transaction.getCategory().getId()
                        : null,
                transaction.getCategory() != null
                        ? transaction.getCategory().getName()
                        : null
        );
    }
}
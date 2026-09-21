package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.IncomeRequest;
import com.syed.finance_tracker.Dto.IncomeResponse;
import com.syed.finance_tracker.Repository.IncomeRepository;
import com.syed.finance_tracker.Repository.TransactionRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.Income;
import com.syed.finance_tracker.entity.Transaction;
import com.syed.finance_tracker.entity.TransactionType;
import com.syed.finance_tracker.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public IncomeService(
            IncomeRepository incomeRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository) {

        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String createIncome(IncomeRequest incomeRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Income income = new Income();

        income.setAmount(incomeRequest.getAmount());
        income.setDescription(incomeRequest.getDescription());
        income.setDate(incomeRequest.getDate());
        income.setUser(user);

        Income savedIncome = incomeRepository.save(income);

        Transaction transaction = new Transaction();

        transaction.setSourceId(savedIncome.getId());
        transaction.setAmount(savedIncome.getAmount());
        transaction.setDescription(savedIncome.getDescription());
        transaction.setDate(savedIncome.getDate());
        transaction.setType(TransactionType.INCOME);
        transaction.setUser(user);

        transactionRepository.save(transaction);

        return "income added";
    }

    public List<IncomeResponse> getIncomes() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Income> incomes =
                incomeRepository.findByUser_Id(user.getId());

        return incomes.stream()
                .map(income -> new IncomeResponse(
                        income.getId(),
                        income.getAmount(),
                        income.getDescription(),
                        income.getDate()
                ))
                .toList();
    }

    public IncomeResponse getIncomeById(Long incomeId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Income income = incomeRepository.findByIdAndUser_Id(
                        incomeId,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Income not found"));

        return new IncomeResponse(
                income.getId(),
                income.getAmount(),
                income.getDescription(),
                income.getDate()
        );
    }

    @Transactional
    public IncomeResponse updateIncome(
            Long incomeId,
            IncomeRequest incomeRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Income income = incomeRepository.findByIdAndUser_Id(
                        incomeId,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Income not found"));

        income.setAmount(incomeRequest.getAmount());
        income.setDescription(incomeRequest.getDescription());
        income.setDate(incomeRequest.getDate());

        Income updatedIncome = incomeRepository.save(income);

        Transaction transaction = transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        incomeId,
                        TransactionType.INCOME,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(updatedIncome.getAmount());
        transaction.setDescription(updatedIncome.getDescription());
        transaction.setDate(updatedIncome.getDate());

        transactionRepository.save(transaction);

        return new IncomeResponse(
                updatedIncome.getId(),
                updatedIncome.getAmount(),
                updatedIncome.getDescription(),
                updatedIncome.getDate()
        );
    }

    @Transactional
    public String deleteIncome(Long incomeId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Income income = incomeRepository.findByIdAndUser_Id(
                        incomeId,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Income not found"));

        Transaction transaction = transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        incomeId,
                        TransactionType.INCOME,
                        user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(transaction);

        incomeRepository.delete(income);

        return "income deleted";
    }
}
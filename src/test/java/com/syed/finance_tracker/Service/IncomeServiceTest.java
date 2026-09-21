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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private IncomeService incomeService;


    @Test
    void createIncome_shouldSaveIncomeAndTransaction() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        IncomeRequest request = new IncomeRequest();
        request.setAmount(new BigDecimal("50000"));
        request.setDescription("Salary");
        request.setDate(LocalDate.now());

        Income income = new Income();
        income.setId(1L);
        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());
        income.setDate(request.getDate());
        income.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(incomeRepository.save(any(Income.class)))
                .thenReturn(income);

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(new Transaction());

        String result = incomeService.createIncome(request);

        assertEquals("income added", result);

        verify(incomeRepository)
                .save(any(Income.class));

        verify(transactionRepository)
                .save(any(Transaction.class));
    }

    @Test
    void createIncome_userNotFound_shouldThrowException() {

        String email = "user@example.com";

        IncomeRequest request = new IncomeRequest();
        request.setAmount(new BigDecimal("50000"));
        request.setDescription("Salary");
        request.setDate(LocalDate.now());

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> incomeService.createIncome(request)
        );

        verify(incomeRepository, never())
                .save(any(Income.class));

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }

    @Test
    void getIncomes_shouldReturnUserIncomes() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Income income1 = new Income();
        income1.setId(1L);
        income1.setAmount(new BigDecimal("50000"));
        income1.setDescription("Salary");
        income1.setDate(LocalDate.now());
        income1.setUser(user);

        Income income2 = new Income();
        income2.setId(2L);
        income2.setAmount(new BigDecimal("10000"));
        income2.setDescription("Freelance");
        income2.setDate(LocalDate.now());
        income2.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(incomeRepository.findByUser_Id(1L))
                .thenReturn(java.util.List.of(income1, income2));

        var result = incomeService.getIncomes();

        assertEquals(2, result.size());

        assertEquals(
                new BigDecimal("50000"),
                result.get(0).getAmount()
        );

        assertEquals(
                "Salary",
                result.get(0).getDescription()
        );

        assertEquals(
                new BigDecimal("10000"),
                result.get(1).getAmount()
        );

        assertEquals(
                "Freelance",
                result.get(1).getDescription()
        );

        verify(incomeRepository)
                .findByUser_Id(1L);
    }

    @Test
    void updateIncome_shouldUpdateIncomeAndTransaction() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Income income = new Income();
        income.setId(10L);
        income.setAmount(new BigDecimal("50000"));
        income.setDescription("Old Salary");
        income.setDate(LocalDate.now());
        income.setUser(user);

        IncomeRequest request = new IncomeRequest();
        request.setAmount(new BigDecimal("60000"));
        request.setDescription("Updated Salary");
        request.setDate(LocalDate.now());

        Transaction transaction = new Transaction();
        transaction.setId(20L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(incomeRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(income));

        when(incomeRepository.save(any(Income.class)))
                .thenReturn(income);

        when(transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        10L,
                        TransactionType.INCOME,
                        1L))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        IncomeResponse result =
                incomeService.updateIncome(10L, request);

        assertEquals(
                new BigDecimal("60000"),
                result.getAmount()
        );

        assertEquals(
                "Updated Salary",
                result.getDescription()
        );

        verify(incomeRepository)
                .save(income);

        verify(transactionRepository)
                .save(transaction);
    }

    @Test
    void deleteIncome_shouldDeleteIncomeAndTransaction() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Income income = new Income();
        income.setId(10L);
        income.setUser(user);

        Transaction transaction = new Transaction();
        transaction.setId(20L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(incomeRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(income));

        when(transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        10L,
                        TransactionType.INCOME,
                        1L))
                .thenReturn(Optional.of(transaction));

        String result = incomeService.deleteIncome(10L);

        assertEquals("income deleted", result);

        verify(transactionRepository)
                .delete(transaction);

        verify(incomeRepository)
                .delete(income);
    }


    @Test
    void deleteIncome_notFound_shouldThrowException() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(incomeRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> incomeService.deleteIncome(10L)
        );

        verify(incomeRepository, never())
                .delete(any(Income.class));

        verify(transactionRepository, never())
                .delete(any(Transaction.class));
    }
}

package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.ExpenseResponse;
import com.syed.finance_tracker.Repository.CategoryRepository;
import com.syed.finance_tracker.Repository.ExpenseRepository;
import com.syed.finance_tracker.Repository.TransactionRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.never;

import static org.mockito.Mockito.anyLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syed.finance_tracker.Dto.ExpenseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetAlertService budgetAlertService;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void createExpense_shouldSaveExpenseAndTransaction() {

        User user = new User(
                "Syed",
                "syed@example.com",
                "hashedPassword"
        );

        Category category = new Category();
        category.setName("Food");
        category.setUser(user);

        ExpenseRequest request = new ExpenseRequest();

        // Set the request values using your existing setters
        request.setAmount(new BigDecimal("500"));
        request.setDescription("Lunch");
        request.setDate(LocalDate.of(2026, 9, 18));
        request.setCategoryId(1L);

        Authentication authentication =
                mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("syed@example.com");

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("syed@example.com"))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findByIdAndUser_Id(1L, user.getId()))
                .thenReturn(Optional.of(category));

        Expense savedExpense = new Expense();
        savedExpense.setAmount(request.getAmount());
        savedExpense.setDescription(request.getDescription());
        savedExpense.setDate(request.getDate());
        savedExpense.setUser(user);
        savedExpense.setCategory(category);

        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(savedExpense);

        String result = expenseService.createExpense(request);

        assertEquals("expense added", result);

        verify(expenseRepository).save(any(Expense.class));
        verify(transactionRepository).save(any(Transaction.class));
        verify(budgetAlertService).createBudgetNotification(savedExpense);
    }

    @Test
    void createExpense_categoryNotFound_shouldThrowException() {

        // Arrange
        String email = "user@example.com";

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("500"));
        request.setDescription("Lunch");
        request.setDate(LocalDate.now());
        request.setCategoryId(5L);

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

        when(categoryRepository.findByIdAndUser_Id(5L, 1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> {
            expenseService.createExpense(request);
        });

        // Verify nothing was saved
        verify(expenseRepository, never()).save(any(Expense.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createExpense_userNotFound_shouldThrowException() {

        // Arrange
        String email = "user@example.com";

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("500"));
        request.setDescription("Lunch");
        request.setDate(LocalDate.now());
        request.setCategoryId(5L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            expenseService.createExpense(request);
        });

        // Verify nothing was saved
        verify(expenseRepository, never()).save(any(Expense.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createExpense_expenseSaveFails_shouldThrowException() {

        // Arrange
        String email = "user@example.com";

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("500"));
        request.setDescription("Lunch");
        request.setDate(LocalDate.now());
        request.setCategoryId(5L);

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category category = new Category();
        category.setId(5L);
        category.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findByIdAndUser_Id(5L, 1L))
                .thenReturn(Optional.of(category));

        when(expenseRepository.save(any(Expense.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> {
            expenseService.createExpense(request);
        });

        // Expense failed, so these should never happen
        verify(transactionRepository, never())
                .save(any(Transaction.class));

        verify(budgetAlertService, never())
                .createBudgetNotification(any(Expense.class));
    }

    @Test
    void createExpense_transactionSaveFails_shouldThrowException() {

        // Arrange
        String email = "user@example.com";

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("500"));
        request.setDescription("Lunch");
        request.setDate(LocalDate.now());
        request.setCategoryId(5L);

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category category = new Category();
        category.setId(5L);
        category.setUser(user);

        Expense savedExpense = new Expense();
        savedExpense.setId(10L);
        savedExpense.setAmount(request.getAmount());
        savedExpense.setDescription(request.getDescription());
        savedExpense.setDate(request.getDate());
        savedExpense.setUser(user);
        savedExpense.setCategory(category);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findByIdAndUser_Id(5L, 1L))
                .thenReturn(Optional.of(category));

        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(savedExpense);

        when(transactionRepository.save(any(Transaction.class)))
                .thenThrow(new RuntimeException("Transaction save failed"));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> {
            expenseService.createExpense(request);
        });

        verify(expenseRepository)
                .save(any(Expense.class));

        verify(transactionRepository)
                .save(any(Transaction.class));
    }






    @Test
    void getExpenses_shouldReturnUserExpenses() {

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

        Category category = new Category();
        category.setId(5L);
        category.setUser(user);

        Expense expense = new Expense();
        expense.setId(10L);
        expense.setAmount(new BigDecimal("500"));
        expense.setDescription("Lunch");
        expense.setUser(user);

        expense.setCategory(category);

        when(expenseRepository.findByUser_Id(1L))
                .thenReturn(List.of(expense));

        List<ExpenseResponse> result = expenseService.getExpenses();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("500"), result.get(0).getAmount());
        assertEquals("Lunch", result.get(0).getDescription());

        verify(expenseRepository).findByUser_Id(1L);
    }

    @Test
    void getExpenses_userNotFound_shouldThrowException() {

        String email = "user@example.com";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            expenseService.getExpenses();
        });

        verify(expenseRepository, never()).findByUser_Id(anyLong());
    }





    @Test
    void getExpenseById_shouldReturnExpense() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category category = new Category();
        category.setId(5L);
        category.setUser(user);

        Expense expense = new Expense();
        expense.setId(10L);
        expense.setAmount(new BigDecimal("500"));
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.now());
        expense.setUser(user);
        expense.setCategory(category);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(expense));

        ExpenseResponse result = expenseService.getExpenseById(10L);

        assertEquals(10L, result.getId());
        assertEquals(new BigDecimal("500"), result.getAmount());
        assertEquals("Lunch", result.getDescription());

        verify(expenseRepository).findByIdAndUser_Id(10L, 1L);
    }

    @Test
    void getExpenseById_notFound_shouldThrowException() {

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

        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            expenseService.getExpenseById(10L);
        });

        verify(expenseRepository)
                .findByIdAndUser_Id(10L, 1L);
    }





    @Test
    void updateExpense_shouldUpdateExpense() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category oldCategory = new Category();
        oldCategory.setId(5L);
        oldCategory.setName("Food");
        oldCategory.setUser(user);

        Category newCategory = new Category();
        newCategory.setId(6L);
        newCategory.setName("Bills");
        newCategory.setUser(user);

        Expense expense = new Expense();
        expense.setId(10L);
        expense.setAmount(new BigDecimal("500"));
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.now());
        expense.setUser(user);
        expense.setCategory(oldCategory);

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("700"));
        request.setDescription("Dinner");
        request.setDate(LocalDate.now());
        request.setCategoryId(6L);

        Transaction transaction = new Transaction();
        transaction.setId(20L);
        transaction.setSourceId(10L);
        transaction.setType(TransactionType.EXPENSE);
        transaction.setUser(user);
        transaction.setCategory(oldCategory);

        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        // Mock user lookup
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        // Mock expense lookup
        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(expense));

        // Mock category lookup
        when(categoryRepository.findByIdAndUser_Id(6L, 1L))
                .thenReturn(Optional.of(newCategory));

        // Mock expense save
        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(expense);

        // Mock transaction lookup
        when(transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        10L,
                        TransactionType.EXPENSE,
                        1L))
                .thenReturn(Optional.of(transaction));

        // Mock transaction save
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        // Act
        ExpenseResponse result =
                expenseService.updateExpense(10L, request);

        // Assert
        assertEquals(new BigDecimal("700"), result.getAmount());
        assertEquals("Dinner", result.getDescription());

        assertEquals(6L, expense.getCategory().getId());
        assertEquals("Bills", expense.getCategory().getName());

        assertEquals(6L, transaction.getCategory().getId());
        assertEquals("Bills", transaction.getCategory().getName());

        verify(expenseRepository).save(expense);
        verify(transactionRepository).save(transaction);
        verify(budgetAlertService)
                .createBudgetNotification(expense);
    }

    @Test
    void updateExpense_notFound_shouldThrowException() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("700"));
        request.setDescription("Dinner");
        request.setDate(LocalDate.now());
        request.setCategoryId(6L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            expenseService.updateExpense(10L, request);
        });

        verify(expenseRepository, never())
                .save(any(Expense.class));
    }






//    @Test
//    void updateExpense_notFound_shouldThrowException() {
//
//        String email = "user@example.com";
//
//        User user = new User(
//                "Test User",
//                email,
//                "hashed-password"
//        );
//        user.setId(1L);
//
//        ExpenseRequest request = new ExpenseRequest();
//        request.setAmount(new BigDecimal("700"));
//        request.setDescription("Dinner");
//        request.setDate(LocalDate.now());
//        request.setCategoryId(6L);
//
//        Authentication authentication = mock(Authentication.class);
//        SecurityContext securityContext = mock(SecurityContext.class);
//
//        when(securityContext.getAuthentication())
//                .thenReturn(authentication);
//
//        when(authentication.getName())
//                .thenReturn(email);
//
//        SecurityContextHolder.setContext(securityContext);
//
//        when(userRepository.findByEmail(email))
//                .thenReturn(Optional.of(user));
//
//        // Expense doesn't exist / doesn't belong to this user
//        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
//                .thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> {
//            expenseService.updateExpense(10L, request);
//        });
//
//        verify(expenseRepository, never())
//                .save(any(Expense.class));
//    }








    @Test
    void deleteExpense_shouldDeleteExpenseAndTransaction() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Expense expense = new Expense();
        expense.setId(10L);
        expense.setUser(user);

        Transaction transaction = new Transaction();
        transaction.setId(20L);
        transaction.setUser(user);
        transaction.setSourceId(10L);
        transaction.setType(TransactionType.EXPENSE);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(expense));

        when(transactionRepository
                .findBySourceIdAndTypeAndUser_Id(
                        10L,
                        TransactionType.EXPENSE,
                        1L))
                .thenReturn(Optional.of(transaction));

        expenseService.deleteExpense(10L);

        verify(expenseRepository).delete(expense);
        verify(transactionRepository).delete(transaction);
    }




    @Test
    void deleteExpense_notFound_shouldThrowException() {

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

        when(expenseRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            expenseService.deleteExpense(10L);
        });

        verify(expenseRepository, never())
                .delete(any(Expense.class));
    }

}
package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.BudgetRequest;
import com.syed.finance_tracker.Dto.BudgetResponse;
import com.syed.finance_tracker.Repository.BudgetRepository;
import com.syed.finance_tracker.Repository.CategoryRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.Budget;
import com.syed.finance_tracker.entity.Category;
import com.syed.finance_tracker.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BudgetService budgetService;


    @Test
    void createBudget_shouldSaveBudget() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category category = new Category();
        category.setId(5L);
        category.setName("Food");
        category.setUser(user);

        BudgetRequest request = new BudgetRequest();
        request.setAmount(new BigDecimal("5000"));
        request.setMonth(LocalDate.of(2026, 9, 1));
        request.setCategoryId(5L);

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

        when(budgetRepository.save(any(Budget.class)))
                .thenReturn(new Budget());

        String result = budgetService.createBudget(request);

        assertEquals("budget added", result);

        verify(budgetRepository)
                .save(any(Budget.class));
    }

    @Test
    void getBudgets_shouldReturnBudgets() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category category = new Category();
        category.setId(5L);
        category.setName("Food");
        category.setUser(user);

        Budget budget = new Budget();
        budget.setId(10L);
        budget.setAmount(new BigDecimal("5000"));
        budget.setMonth(LocalDate.of(2026, 9, 1));
        budget.setUser(user);
        budget.setCategory(category);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(budgetRepository.findByUser_Id(1L))
                .thenReturn(List.of(budget));

        List<BudgetResponse> result =
                budgetService.getBudgets();

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals(new BigDecimal("5000"), result.get(0).getAmount());
        assertEquals(5L, result.get(0).getCategoryId());
        assertEquals("Food", result.get(0).getCategoryName());

        verify(budgetRepository)
                .findByUser_Id(1L);
    }

    @Test
    void updateBudget_shouldUpdateBudget() {

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
        newCategory.setName("Transport");
        newCategory.setUser(user);

        Budget budget = new Budget();
        budget.setId(10L);
        budget.setAmount(new BigDecimal("5000"));
        budget.setMonth(LocalDate.of(2026, 9, 1));
        budget.setUser(user);
        budget.setCategory(oldCategory);

        BudgetRequest request = new BudgetRequest();
        request.setAmount(new BigDecimal("7000"));
        request.setMonth(LocalDate.of(2026, 10, 1));
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

        when(budgetRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(budget));

        when(categoryRepository.findByIdAndUser_Id(6L, 1L))
                .thenReturn(Optional.of(newCategory));

        when(budgetRepository.save(any(Budget.class)))
                .thenReturn(budget);

        BudgetResponse result =
                budgetService.updateBudget(10L, request);

        assertEquals(10L, result.getId());
        assertEquals(new BigDecimal("7000"), result.getAmount());
        assertEquals(LocalDate.of(2026, 10, 1), result.getMonth());
        assertEquals(6L, result.getCategoryId());
        assertEquals("Transport", result.getCategoryName());

        verify(budgetRepository)
                .save(budget);
    }

    @Test
    void deleteBudget_shouldDeleteBudget() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Budget budget = new Budget();
        budget.setId(10L);
        budget.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(budgetRepository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(budget));

        String result = budgetService.deleteBudget(10L);

        assertEquals("budget deleted", result);

        verify(budgetRepository).delete(budget);
    }
}

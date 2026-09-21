package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.CategoryRequest;
import com.syed.finance_tracker.Dto.CategoryResponse;
import com.syed.finance_tracker.Repository.CategoryRepository;
import com.syed.finance_tracker.Repository.UserRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    void createCategory_shouldSaveCategory() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        CategoryRequest request = new CategoryRequest();
        request.setName("Food");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(new Category());

        String result = categoryService.createCategory(request);

        assertEquals("category added", result);

        verify(categoryRepository)
                .save(any(Category.class));
    }

    @Test
    void getCategories_shouldReturnUserCategories() {

        String email = "user@example.com";

        User user = new User(
                "Test User",
                email,
                "hashed-password"
        );
        user.setId(1L);

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Food");
        category1.setUser(user);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Transport");
        category2.setUser(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findByUser_Id(1L))
                .thenReturn(java.util.List.of(category1, category2));

        var result = categoryService.getCategories();

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Food", result.get(0).getName());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Transport", result.get(1).getName());

        verify(categoryRepository)
                .findByUser_Id(1L);
    }

    @Test
    void updateCategory_shouldUpdateCategory() {

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

        CategoryRequest request = new CategoryRequest();
        request.setName("Groceries");

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

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponse result =
                categoryService.updateCategory(5L, request);

        assertEquals(5L, result.getId());
        assertEquals("Groceries", result.getName());

        verify(categoryRepository)
                .save(category);
    }


    @Test
    void deleteCategory_shouldDeleteCategory() {

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

        String result =
                categoryService.deleteCategory(5L);

        assertEquals("category deleted", result);

        verify(categoryRepository)
                .delete(category);
    }
}

package com.syed.finance_tracker.Service;

import com.syed.finance_tracker.Dto.CategoryRequest;
import com.syed.finance_tracker.Dto.CategoryResponse;
import com.syed.finance_tracker.Repository.CategoryRepository;
import com.syed.finance_tracker.Repository.UserRepository;
import com.syed.finance_tracker.entity.Category;
import com.syed.finance_tracker.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public String createCategory(CategoryRequest categoryRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = new Category();

        category.setName(categoryRequest.getName());
        category.setUser(user);

        categoryRepository.save(category);

        return "category added";
    }

    public List<CategoryResponse> getCategories() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Category> categories =
                categoryRepository.findByUser_Id(user.getId());

        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    public CategoryResponse getCategoryById(Long categoryId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest categoryRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryRequest.getName());

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                updatedCategory.getId(),
                updatedCategory.getName()
        );
    }

    public String deleteCategory(Long categoryId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);

        return "category deleted";
    }
}
package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.CategoryRequest;
import com.syed.finance_tracker.Dto.CategoryResponse;
import com.syed.finance_tracker.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public String addCategory(
            @RequestBody CategoryRequest categoryRequest) {

        return categoryService.createCategory(categoryRequest);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryResponse getCategoryById(
            @PathVariable Long categoryId) {

        return categoryService.getCategoryById(categoryId);
    }

    @PutMapping("/categories/{categoryId}")
    public CategoryResponse updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest categoryRequest) {

        return categoryService.updateCategory(
                categoryId,
                categoryRequest
        );
    }

    @DeleteMapping("/categories/{categoryId}")
    public String deleteCategory(
            @PathVariable Long categoryId) {

        return categoryService.deleteCategory(categoryId);
    }
}

package com.syed.finance_tracker.Repository;

import com.syed.finance_tracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser_Id(Long userId);

    Optional<Category> findByIdAndUser_Id(Long categoryId, Long userId);
}
package com.syed.finance_tracker.Repository;

import com.syed.finance_tracker.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByUser_IdAndCategory_IdAndMonthAndType(
            Long userId,
            Long categoryId,
            LocalDate month,
            String type
    );

    List<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
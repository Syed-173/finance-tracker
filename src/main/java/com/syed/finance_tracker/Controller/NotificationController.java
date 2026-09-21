package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.NotificationResponse;
import com.syed.finance_tracker.Service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getNotifications() {

        return notificationService.getNotifications();
    }

    @PatchMapping("/notifications/{notificationId}/read")
    public String markAsRead(
            @PathVariable Long notificationId) {

        notificationService.markAsRead(notificationId);

        return "notification marked as read";
    }
}

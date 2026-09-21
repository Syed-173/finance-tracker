package com.syed.finance_tracker.Dto;

import java.time.Instant;

public class NotificationResponse {

    private Long id;
    private String message;
    private String type;
    private boolean read;
    private Instant createdAt;

    public NotificationResponse(
            Long id,
            String message,
            String type,
            boolean read,
            Instant createdAt) {

        this.id = id;
        this.message = message;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
package com.danilodps.commons.domain.model.error;

import java.time.LocalDateTime;

public class StandardErrorDetails {

    private String message;
    private LocalDateTime timestamp;

    public StandardErrorDetails() {
    }

    public StandardErrorDetails(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "StandardErrorDetails{" +
                "message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}

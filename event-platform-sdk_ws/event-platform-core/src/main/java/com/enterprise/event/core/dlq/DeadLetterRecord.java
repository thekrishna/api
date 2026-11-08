package com.enterprise.event.core.dlq;

import java.io.Serializable;
import java.time.Instant;

public record DeadLetterRecord(
        String eventId,
        String eventType,
        String errorCode,
        String message,
        Instant createdAt,
        String payload,
        String correlationId,
        String exceptionType,
        String stackTrace
) implements Serializable {

    public DeadLetterRecord(String eventId, String eventType, String errorCode, String message, Instant createdAt) {
        this(eventId, eventType, errorCode, message, createdAt, null, null, null, null);
    }
}

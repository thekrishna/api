package com.eventplatform.exception;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class EventPlatformException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EventPlatformErrorCode errorCode;
    private final EventPlatformExceptionCategory category;
    private final EventPlatformExceptionAction action;
    private final String eventId;
    private final String eventType;
    private final String aggregateId;
    private final String correlationId;
    private final String tenantId;
    private final String traceId;
    private final String source;

    public EventPlatformException(
            EventPlatformErrorCode errorCode,
            String message,
            EventPlatformExceptionContext context,
            Throwable cause
    ) {
        super(message, cause);
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
        this.category = errorCode.category();
        this.action = errorCode.defaultAction();
        this.eventId = context.eventId();
        this.eventType = context.eventType();
        this.aggregateId = context.aggregateId();
        this.correlationId = context.correlationId();
        this.tenantId = context.tenantId();
        this.traceId = context.traceId();
        this.source = context.source();
    }

    public EventPlatformException(
            EventPlatformErrorCode errorCode,
            String message,
            EventPlatformExceptionContext context
    ) {
        this(errorCode, message, context, null);
    }

    public EventPlatformException(
            EventPlatformErrorCode errorCode,
            EventPlatformExceptionContext context,
            Throwable cause
    ) {
        this(errorCode, errorCode.name(), context, cause);
    }

    public EventPlatformException(
            EventPlatformErrorCode errorCode,
            String message
    ) {
        this(errorCode, message, new EventPlatformExceptionContext("unknown", "unknown", "unknown", "unknown", "unknown", "unknown", "unknown"), null);
    }

    public EventPlatformException(
            EventPlatformErrorCode errorCode,
            Throwable cause
    ) {
        this(errorCode, errorCode.name(), new EventPlatformExceptionContext("unknown", "unknown", "unknown", "unknown", "unknown", "unknown", "unknown"), cause);
    }

    public boolean isRetriable() {
        return action == EventPlatformExceptionAction.RETRY || action == EventPlatformExceptionAction.PENDING;
    }

    public boolean isDlqCandidate() {
        return action == EventPlatformExceptionAction.DLQ;
    }
}

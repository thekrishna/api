package com.eventplatform.exception;

public enum EventPlatformErrorCode {
    EVENT_VALIDATION_FAILED(EventPlatformExceptionCategory.VALIDATION, EventPlatformExceptionAction.IGNORE),
    EVENT_SERIALIZATION_FAILED(EventPlatformExceptionCategory.SERIALIZATION, EventPlatformExceptionAction.DLQ),
    EVENT_DESERIALIZATION_FAILED(EventPlatformExceptionCategory.SERIALIZATION, EventPlatformExceptionAction.DLQ),
    EVENT_PROCESSING_FAILED(EventPlatformExceptionCategory.PROCESSING, EventPlatformExceptionAction.RETRY),
    EVENT_PERSISTENCE_FAILED(EventPlatformExceptionCategory.INFRASTRUCTURE, EventPlatformExceptionAction.RETRY),
    EVENT_CONFIGURATION_INVALID(EventPlatformExceptionCategory.CONFIGURATION, EventPlatformExceptionAction.FAIL),
    EVENT_RETRY_EXHAUSTED(EventPlatformExceptionCategory.RETRY, EventPlatformExceptionAction.DLQ),
    EVENT_IDEMPOTENT_REJECTION(EventPlatformExceptionCategory.BUSINESS, EventPlatformExceptionAction.IGNORE),
    EVENT_REPLAY_FAILED(EventPlatformExceptionCategory.PROCESSING, EventPlatformExceptionAction.PENDING),
    EVENT_DLQ_PROCESSED(EventPlatformExceptionCategory.DLQ, EventPlatformExceptionAction.IGNORE),
    EVENT_UNKNOWN_FAILURE(EventPlatformExceptionCategory.UNKNOWN, EventPlatformExceptionAction.RETRY);

    private final EventPlatformExceptionCategory category;
    private final EventPlatformExceptionAction defaultAction;

    EventPlatformErrorCode(EventPlatformExceptionCategory category, EventPlatformExceptionAction defaultAction) {
        this.category = category;
        this.defaultAction = defaultAction;
    }

    public EventPlatformExceptionCategory category() {
        return category;
    }

    public EventPlatformExceptionAction defaultAction() {
        return defaultAction;
    }
}

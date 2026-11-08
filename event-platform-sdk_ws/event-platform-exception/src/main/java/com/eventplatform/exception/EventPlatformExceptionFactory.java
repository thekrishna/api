package com.eventplatform.exception;

public final class EventPlatformExceptionFactory {

    private EventPlatformExceptionFactory() {
    }

    public static EventPlatformException invalidEvent(String message, EventPlatformExceptionContext context) {
        return new EventPlatformException(EventPlatformErrorCode.EVENT_VALIDATION_FAILED, message, context);
    }

    public static EventPlatformException serializationFailure(String message, EventPlatformExceptionContext context, Throwable cause) {
        return new EventPlatformException(EventPlatformErrorCode.EVENT_SERIALIZATION_FAILED, message, context, cause);
    }

    public static EventPlatformException processingFailure(String message, EventPlatformExceptionContext context, Throwable cause) {
        return new EventPlatformException(EventPlatformErrorCode.EVENT_PROCESSING_FAILED, message, context, cause);
    }

    public static EventPlatformException persistenceFailure(String message, EventPlatformExceptionContext context, Throwable cause) {
        return new EventPlatformException(EventPlatformErrorCode.EVENT_PERSISTENCE_FAILED, message, context, cause);
    }

    public static EventPlatformException configurationFailure(String message, EventPlatformExceptionContext context) {
        return new EventPlatformException(EventPlatformErrorCode.EVENT_CONFIGURATION_INVALID, message, context);
    }

    public static EventPlatformException retryExhausted(String message, EventPlatformExceptionContext context) {
        return new EventPlatformException(EventPlatformErrorCode.EVENT_RETRY_EXHAUSTED, message, context);
    }
}

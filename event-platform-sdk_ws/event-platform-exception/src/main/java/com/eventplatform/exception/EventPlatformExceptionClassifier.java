package com.eventplatform.exception;

import java.util.Objects;

public final class EventPlatformExceptionClassifier {

    private EventPlatformExceptionClassifier() {
    }

    public static EventPlatformExceptionClassification classify(Throwable throwable) {
        Objects.requireNonNull(throwable, "throwable must not be null");

        if (throwable instanceof EventPlatformException eventException) {
            return new EventPlatformExceptionClassification(eventException.getCategory(), eventException.getAction());
        }

        String message = throwable.getMessage() == null ? "" : throwable.getMessage().toLowerCase();
        if (message.contains("serialization") || message.contains("json") || message.contains("marshal")) {
            return new EventPlatformExceptionClassification(
                    EventPlatformExceptionCategory.SERIALIZATION,
                    EventPlatformExceptionAction.DLQ
            );
        }
        if (message.contains("validation") || message.contains("invalid") || message.contains("constraint")) {
            return new EventPlatformExceptionClassification(
                    EventPlatformExceptionCategory.VALIDATION,
                    EventPlatformExceptionAction.IGNORE
            );
        }
        if (message.contains("retry") || message.contains("timeout") || message.contains("temporar")) {
            return new EventPlatformExceptionClassification(
                    EventPlatformExceptionCategory.RETRY,
                    EventPlatformExceptionAction.RETRY
            );
        }

        return new EventPlatformExceptionClassification(
                EventPlatformExceptionCategory.UNKNOWN,
                EventPlatformExceptionAction.RETRY
        );
    }
}

package com.eventplatform.core;

import com.eventplatform.exception.EventPlatformExceptionAction;

import java.io.Serializable;

public record EventProcessingResult(
        String eventId,
        String eventType,
        boolean success,
        EventPlatformExceptionAction action,
        String message
) implements Serializable {
}

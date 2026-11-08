package com.eventplatform.exception;

import java.io.Serializable;
import java.util.Objects;

public record EventPlatformExceptionContext(
        String eventId,
        String eventType,
        String aggregateId,
        String correlationId,
        String tenantId,
        String traceId,
        String source
) implements Serializable {

    public EventPlatformExceptionContext {
        eventId = Objects.requireNonNullElse(eventId, "unknown");
        eventType = Objects.requireNonNullElse(eventType, "unknown");
        aggregateId = Objects.requireNonNullElse(aggregateId, "unknown");
        correlationId = Objects.requireNonNullElse(correlationId, "unknown");
        tenantId = Objects.requireNonNullElse(tenantId, "unknown");
        traceId = Objects.requireNonNullElse(traceId, "unknown");
        source = Objects.requireNonNullElse(source, "unknown");
    }
}

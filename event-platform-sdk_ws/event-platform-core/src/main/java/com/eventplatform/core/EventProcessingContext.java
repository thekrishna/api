package com.eventplatform.core;

import java.io.Serializable;

public record EventProcessingContext(
        String tenantId,
        String traceId,
        String correlationId,
        String source,
        String aggregateId,
        String eventType
) implements Serializable {
}

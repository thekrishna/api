package com.enterprise.event.core.pending;

import java.io.Serializable;
import java.time.Instant;

public record PendingEvent(
        String eventId,
        String eventType,
        int retryCount,
        Instant nextRetryAt,
        String status
) implements Serializable {
}

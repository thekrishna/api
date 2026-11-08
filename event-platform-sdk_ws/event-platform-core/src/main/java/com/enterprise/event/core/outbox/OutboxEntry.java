package com.enterprise.event.core.outbox;

import java.io.Serializable;
import java.time.Instant;

public record OutboxEntry(
        String id,
        String eventType,
        String payload,
        Instant createdAt,
        String status
) implements Serializable {
}

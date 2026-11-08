package com.enterprise.event.core.inbox;

import java.io.Serializable;
import java.time.Instant;

public record InboxEntry(
        String eventId,
        String eventType,
        String consumerId,
        Instant createdAt,
        String status
) implements Serializable {
}

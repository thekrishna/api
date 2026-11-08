package com.enterprise.event.core.state;

import java.io.Serializable;
import java.time.Instant;

public record EventStateRecord(
        String eventId,
        EventLifecycleState state,
        Instant updatedAt
) implements Serializable {
}

package com.fnol.common;

import java.time.Instant;

public record ClaimEventEnvelope<T>(
        String eventId,
        EventType eventType,
        String eventVersion,
        String claimId,
        Instant timestamp,
        String source,
        T payload
) {
}

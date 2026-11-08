package com.eventplatform.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class GenericEventEnvelope<T> implements Serializable {

    private final EventIdentifier eventId;
    private final String eventType;
    private final EventTypeVersion eventTypeVersion;
    private final String source;
    private final EventStatus status;
    private final Instant occurredAt;
    private final EventMetadata metadata;
    private final EventHeaders headers;
    private final T payload;
    private final EventCorrelation correlation;
    private final EventContext context;
    private final String schemaVersion;
    private final String aggregateId;
}

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
public class EventMetadata implements Serializable {

    private final String eventType;
    private final String eventVersion;
    private final String source;
    private final String aggregateType;
    private final String aggregateId;
    private final Instant occurredAt;
    private final String schemaVersion;
    private final String createdBy;
    private final String description;

    public String eventType() {
        return eventType;
    }

    public String eventVersion() {
        return eventVersion;
    }

    public String source() {
        return source;
    }

    public String aggregateType() {
        return aggregateType;
    }

    public String aggregateId() {
        return aggregateId;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public String schemaVersion() {
        return schemaVersion;
    }

    public String createdBy() {
        return createdBy;
    }

    public String description() {
        return description;
    }
}

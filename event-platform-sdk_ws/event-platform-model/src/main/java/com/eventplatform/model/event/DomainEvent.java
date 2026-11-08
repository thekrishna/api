package com.eventplatform.model.event;

import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent<T> extends Serializable {

    String eventType();

    String aggregateType();

    String aggregateId();

    Instant occurredAt();

    T payload();
}

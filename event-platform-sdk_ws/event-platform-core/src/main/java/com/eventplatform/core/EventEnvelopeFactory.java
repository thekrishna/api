package com.eventplatform.core;

import com.eventplatform.model.event.EventHeaders;
import com.eventplatform.model.event.EventMetadata;
import com.eventplatform.model.event.EventStatus;
import com.eventplatform.model.event.GenericEventEnvelope;

public interface EventEnvelopeFactory {

    <T> GenericEventEnvelope<T> create(
            String eventType,
            String source,
            EventStatus status,
            T payload,
            EventProcessingContext context,
            EventHeaders headers,
            EventMetadata metadata
    );
}

package com.eventplatform.producer;

import com.eventplatform.core.DefaultEventEnvelopeFactory;
import com.eventplatform.core.EventProcessingContext;
import com.eventplatform.core.EventPlatformCoreProperties;
import com.enterprise.event.core.serializer.EventSerializer;
import com.enterprise.event.core.serializer.JsonEventSerializer;
import com.enterprise.event.core.validator.DefaultEventValidator;
import com.enterprise.event.core.validator.EventValidator;
import com.eventplatform.exception.EventPlatformExceptionContext;
import com.eventplatform.exception.EventPlatformExceptionFactory;
import com.eventplatform.model.event.EventHeaders;
import com.eventplatform.model.event.EventMetadata;
import com.eventplatform.model.event.EventStatus;
import com.eventplatform.model.event.GenericEventEnvelope;

import java.util.Objects;

public class DefaultEventProducer<T> implements EventProducer<T> {

    private final EventSerializer<T> serializer;
    private final EventValidator<T> validator;
    private final DefaultEventEnvelopeFactory envelopeFactory;

    public DefaultEventProducer() {
        this(new JsonEventSerializer(), new DefaultEventValidator<>(), new DefaultEventEnvelopeFactory(new EventPlatformCoreProperties()));
    }

    @SuppressWarnings("unchecked")
    public DefaultEventProducer(EventSerializer<?> serializer, EventValidator<?> validator, DefaultEventEnvelopeFactory envelopeFactory) {
        this.serializer = (EventSerializer<T>) Objects.requireNonNull(serializer, "serializer must not be null");
        this.validator = (EventValidator<T>) Objects.requireNonNull(validator, "validator must not be null");
        this.envelopeFactory = Objects.requireNonNull(envelopeFactory, "envelopeFactory must not be null");
    }

    @Override
    public void publish(GenericEventEnvelope<T> envelope) {
        Objects.requireNonNull(envelope, "envelope must not be null");
        validator.validate(envelope.getPayload());
        String serialized = serializer.serialize(envelope.getPayload());
        if (serialized == null || serialized.isBlank()) {
            throw EventPlatformExceptionFactory.processingFailure(
                    "event payload serialization produced empty output",
                    new EventPlatformExceptionContext(envelope.getEventId().value(), envelope.getEventType(), envelope.getAggregateId(), "unknown", "unknown", "unknown", envelope.getSource()),
                    null
            );
        }
    }

    public GenericEventEnvelope<T> createEnvelope(
            String eventType,
            String source,
            EventStatus status,
            T payload,
            EventProcessingContext context,
            EventHeaders headers,
            EventMetadata metadata
    ) {
        return envelopeFactory.create(eventType, source, status, payload, context, headers, metadata);
    }
}

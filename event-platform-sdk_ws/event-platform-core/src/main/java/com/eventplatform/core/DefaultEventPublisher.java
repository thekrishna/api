package com.eventplatform.core;

import com.eventplatform.exception.EventPlatformExceptionAction;
import com.eventplatform.model.event.GenericEventEnvelope;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class DefaultEventPublisher<T> implements EventPublisher<T> {

    private final EventHandlerRegistry registry;

    public DefaultEventPublisher(EventHandlerRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    @Override
    public void publish(GenericEventEnvelope<T> envelope) {
        Objects.requireNonNull(envelope, "envelope must not be null");
        EventHandler<?> handler = registry.getHandler(envelope.getEventType());

        if (handler == null) {
            log.warn("No handler registered for eventType={}", envelope.getEventType());
            return;
        }

        @SuppressWarnings("unchecked")
        EventHandler<T> typedHandler = (EventHandler<T>) handler;
        typedHandler.handle(envelope);

        log.info(
                "Published event eventId={} eventType={} status={} action={} payloadType={}",
                envelope.getEventId(),
                envelope.getEventType(),
                envelope.getStatus(),
                EventPlatformExceptionAction.RETRY,
                envelope.getPayload().getClass().getSimpleName()
        );
    }
}

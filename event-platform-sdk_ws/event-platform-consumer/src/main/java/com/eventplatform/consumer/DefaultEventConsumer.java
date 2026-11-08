package com.eventplatform.consumer;

import com.eventplatform.core.EventHandler;
import com.eventplatform.model.event.GenericEventEnvelope;

import java.util.Objects;

public class DefaultEventConsumer<T> implements EventConsumer<T> {

    private final EventHandler<T> handler;

    public DefaultEventConsumer(EventHandler<T> handler) {
        this.handler = Objects.requireNonNull(handler, "handler must not be null");
    }

    @Override
    public void consume(GenericEventEnvelope<T> envelope) {
        Objects.requireNonNull(envelope, "envelope must not be null");
        handler.handle(envelope);
    }
}

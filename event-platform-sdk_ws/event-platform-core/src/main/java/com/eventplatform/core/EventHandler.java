package com.eventplatform.core;

import com.eventplatform.model.event.GenericEventEnvelope;

@FunctionalInterface
public interface EventHandler<T> {

    void handle(GenericEventEnvelope<T> envelope);
}

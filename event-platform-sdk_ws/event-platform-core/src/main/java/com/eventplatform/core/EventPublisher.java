package com.eventplatform.core;

import com.eventplatform.model.event.GenericEventEnvelope;

public interface EventPublisher<T> {

    void publish(GenericEventEnvelope<T> envelope);
}

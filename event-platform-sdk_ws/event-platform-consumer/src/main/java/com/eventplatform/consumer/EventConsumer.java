package com.eventplatform.consumer;

import com.eventplatform.model.event.GenericEventEnvelope;

public interface EventConsumer<T> {

    void consume(GenericEventEnvelope<T> envelope);
}

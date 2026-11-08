package com.eventplatform.producer;

import com.eventplatform.model.event.GenericEventEnvelope;

public interface EventProducer<T> {

    void publish(GenericEventEnvelope<T> envelope);
}

package com.enterprise.event.core.aggregation;

import com.eventplatform.model.event.GenericEventEnvelope;

import java.util.List;

public interface EventAggregator {

    EventAggregationResult aggregate(String correlationId, List<GenericEventEnvelope<?>> events);
}

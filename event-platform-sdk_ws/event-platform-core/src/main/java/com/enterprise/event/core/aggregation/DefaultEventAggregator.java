package com.enterprise.event.core.aggregation;

import com.eventplatform.model.event.GenericEventEnvelope;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultEventAggregator implements EventAggregator {

    @Override
    public EventAggregationResult aggregate(String correlationId, List<GenericEventEnvelope<?>> events) {
        Map<String, Object> aggregates = new LinkedHashMap<>();
        for (GenericEventEnvelope<?> event : events) {
            aggregates.put(event.getEventId().value(), event.getPayload());
        }
        return new EventAggregationResult(correlationId, aggregates);
    }
}

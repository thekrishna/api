package com.enterprise.event.core.ordering;

import com.eventplatform.model.event.GenericEventEnvelope;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AggregateOrderingService {

    private final Map<String, List<GenericEventEnvelope<?>>> sequences = new ConcurrentHashMap<>();

    public void register(GenericEventEnvelope<?> envelope) {
        Objects.requireNonNull(envelope, "envelope must not be null");
        String aggregateKey = envelope.getMetadata() == null ? "unknown" : envelope.getMetadata().aggregateId();
        sequences.computeIfAbsent(aggregateKey, ignored -> new ArrayList<>()).add(envelope);
    }

    public List<GenericEventEnvelope<?>> orderedFor(String aggregateId) {
        return sequences.getOrDefault(aggregateId, List.of()).stream()
                .sorted(Comparator.comparing(GenericEventEnvelope::getOccurredAt))
                .toList();
    }
}

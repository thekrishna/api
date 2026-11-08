package com.enterprise.event.core.pending;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPendingEventStore implements PendingEventStore {

    private final Map<String, PendingEvent> events = new ConcurrentHashMap<>();

    @Override
    public void save(PendingEvent pendingEvent) {
        events.put(pendingEvent.eventId(), pendingEvent);
    }

    @Override
    public Optional<PendingEvent> find(String eventId) {
        return Optional.ofNullable(events.get(eventId));
    }

    @Override
    public List<PendingEvent> all() {
        return new ArrayList<>(events.values());
    }
}

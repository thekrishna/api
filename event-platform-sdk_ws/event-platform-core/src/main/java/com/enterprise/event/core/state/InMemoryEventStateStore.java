package com.enterprise.event.core.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventStateStore implements EventStateStore {

    private final Map<String, EventStateRecord> states = new ConcurrentHashMap<>();

    @Override
    public void save(EventStateRecord record) {
        states.put(record.eventId(), record);
    }

    @Override
    public Optional<EventStateRecord> find(String eventId) {
        return Optional.ofNullable(states.get(eventId));
    }

    @Override
    public List<EventStateRecord> all() {
        return new ArrayList<>(states.values());
    }
}

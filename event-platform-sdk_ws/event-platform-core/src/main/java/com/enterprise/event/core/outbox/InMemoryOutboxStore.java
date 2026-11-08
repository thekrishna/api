package com.enterprise.event.core.outbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOutboxStore implements OutboxStore {

    private final Map<String, OutboxEntry> entries = new ConcurrentHashMap<>();

    @Override
    public void save(OutboxEntry entry) {
        entries.put(entry.id(), entry);
    }

    @Override
    public Optional<OutboxEntry> findById(String id) {
        return Optional.ofNullable(entries.get(id));
    }

    @Override
    public List<OutboxEntry> pending() {
        return new ArrayList<>(entries.values().stream().filter(entry -> "PENDING".equals(entry.status())).toList());
    }
}

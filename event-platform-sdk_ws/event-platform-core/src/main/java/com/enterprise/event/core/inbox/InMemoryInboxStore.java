package com.enterprise.event.core.inbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryInboxStore implements InboxStore {

    private final Map<String, InboxEntry> entries = new ConcurrentHashMap<>();

    @Override
    public void save(InboxEntry entry) {
        entries.put(entry.eventId(), entry);
    }

    @Override
    public Optional<InboxEntry> findById(String eventId) {
        return Optional.ofNullable(entries.get(eventId));
    }

    @Override
    public List<InboxEntry> all() {
        return new ArrayList<>(entries.values());
    }
}

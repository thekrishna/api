package com.enterprise.event.core.dlq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDeadLetterQueue implements DeadLetterQueue {

    private final Map<String, DeadLetterRecord> records = new ConcurrentHashMap<>();

    @Override
    public void enqueue(DeadLetterRecord record) {
        records.put(record.eventId(), record);
    }

    @Override
    public Optional<DeadLetterRecord> find(String eventId) {
        return Optional.ofNullable(records.get(eventId));
    }

    @Override
    public List<DeadLetterRecord> all() {
        return new ArrayList<>(records.values());
    }
}

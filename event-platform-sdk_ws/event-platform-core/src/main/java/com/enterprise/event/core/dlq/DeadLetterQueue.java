package com.enterprise.event.core.dlq;

import java.util.List;
import java.util.Optional;

public interface DeadLetterQueue {

    void enqueue(DeadLetterRecord record);

    Optional<DeadLetterRecord> find(String eventId);

    default Optional<DeadLetterRecord> receive() {
        return Optional.empty();
    }

    default boolean delete(String eventId) {
        return false;
    }

    List<DeadLetterRecord> all();
}

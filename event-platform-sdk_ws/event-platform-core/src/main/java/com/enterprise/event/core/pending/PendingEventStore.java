package com.enterprise.event.core.pending;

import java.util.List;
import java.util.Optional;

public interface PendingEventStore {

    void save(PendingEvent pendingEvent);

    Optional<PendingEvent> find(String eventId);

    List<PendingEvent> all();
}

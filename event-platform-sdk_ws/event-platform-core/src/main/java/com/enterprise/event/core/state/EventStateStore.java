package com.enterprise.event.core.state;

import java.util.List;
import java.util.Optional;

public interface EventStateStore {

    void save(EventStateRecord record);

    Optional<EventStateRecord> find(String eventId);

    List<EventStateRecord> all();
}

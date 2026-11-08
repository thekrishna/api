package com.enterprise.event.core.inbox;

import java.util.List;
import java.util.Optional;

public interface InboxStore {

    void save(InboxEntry entry);

    Optional<InboxEntry> findById(String eventId);

    List<InboxEntry> all();
}

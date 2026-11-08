package com.enterprise.event.core.outbox;

import java.util.List;
import java.util.Optional;

public interface OutboxStore {

    void save(OutboxEntry entry);

    Optional<OutboxEntry> findById(String id);

    List<OutboxEntry> pending();
}

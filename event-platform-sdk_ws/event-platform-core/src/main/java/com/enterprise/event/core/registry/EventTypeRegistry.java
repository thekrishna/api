package com.enterprise.event.core.registry;

import com.eventplatform.core.EventHandler;

public interface EventTypeRegistry {

    void register(EventTypeDescriptor descriptor, EventHandler<?> handler);

    boolean contains(String eventType);

    EventTypeDescriptor find(String eventType);

    EventHandler<?> handler(String eventType);
}

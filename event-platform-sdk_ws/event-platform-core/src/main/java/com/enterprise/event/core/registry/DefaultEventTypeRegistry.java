package com.enterprise.event.core.registry;

import com.eventplatform.core.EventHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEventTypeRegistry implements EventTypeRegistry {

    private final Map<String, EventTypeDescriptor> descriptors = new ConcurrentHashMap<>();
    private final Map<String, EventHandler<?>> handlers = new ConcurrentHashMap<>();

    @Override
    public void register(EventTypeDescriptor descriptor, EventHandler<?> handler) {
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        Objects.requireNonNull(handler, "handler must not be null");
        descriptors.put(descriptor.eventType(), descriptor);
        handlers.put(descriptor.eventType(), handler);
    }

    @Override
    public boolean contains(String eventType) {
        return descriptors.containsKey(eventType);
    }

    @Override
    public EventTypeDescriptor find(String eventType) {
        return descriptors.get(eventType);
    }

    @Override
    public EventHandler<?> handler(String eventType) {
        return handlers.get(eventType);
    }
}

package com.eventplatform.core;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEventHandlerRegistry implements EventHandlerRegistry {

    private final Map<String, EventHandler<?>> handlers = new ConcurrentHashMap<>();

    @Override
    public void register(String eventType, EventHandler<?> handler) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        Objects.requireNonNull(handler, "handler must not be null");
        handlers.put(eventType, handler);
    }

    @Override
    public boolean hasHandler(String eventType) {
        return handlers.containsKey(eventType);
    }

    @Override
    public EventHandler<?> getHandler(String eventType) {
        return handlers.get(eventType);
    }
}

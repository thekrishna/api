package com.eventplatform.core;

public interface EventHandlerRegistry {

    void register(String eventType, EventHandler<?> handler);

    boolean hasHandler(String eventType);

    EventHandler<?> getHandler(String eventType);
}

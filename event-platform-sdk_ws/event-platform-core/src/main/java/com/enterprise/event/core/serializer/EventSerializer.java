package com.enterprise.event.core.serializer;

public interface EventSerializer<T> {

    String serialize(T payload);

    T deserialize(String content, Class<T> type);
}

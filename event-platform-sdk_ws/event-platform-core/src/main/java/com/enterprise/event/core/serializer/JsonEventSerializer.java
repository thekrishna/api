package com.enterprise.event.core.serializer;

import com.eventplatform.exception.EventPlatformExceptionContext;
import com.eventplatform.exception.EventSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class JsonEventSerializer<T> implements EventSerializer<T> {

    private final ObjectMapper objectMapper;

    public JsonEventSerializer() {
        this(new ObjectMapper());
    }

    public JsonEventSerializer(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    @Override
    public String serialize(T payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException(
                    "Failed to serialize event payload",
                    new EventPlatformExceptionContext("unknown", payload == null ? "unknown" : payload.getClass().getSimpleName(), "unknown", "unknown", "unknown", "unknown", "unknown"),
                    e
            );
        }
    }

    @Override
    public T deserialize(String content, Class<T> type) {
        try {
            return objectMapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException(
                    "Failed to deserialize event payload",
                    new EventPlatformExceptionContext("unknown", type == null ? "unknown" : type.getSimpleName(), "unknown", "unknown", "unknown", "unknown", "unknown"),
                    e
            );
        }
    }
}

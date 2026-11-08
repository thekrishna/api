package com.eventplatform.model.event;

import java.io.Serializable;
import java.util.Objects;

public record EventTypeVersion(String type, String version) implements Serializable {

    public EventTypeVersion {
        Objects.requireNonNull(type, "event type must not be null");
        Objects.requireNonNull(version, "event version must not be null");
        if (type.isBlank()) {
            throw new IllegalArgumentException("event type must not be blank");
        }
        if (version.isBlank()) {
            throw new IllegalArgumentException("event version must not be blank");
        }
    }

    public static EventTypeVersion of(String type, String version) {
        return new EventTypeVersion(type, version);
    }
}

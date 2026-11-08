package com.eventplatform.model.event;

import java.io.Serializable;
import java.util.Objects;

public record EventIdentifier(String value) implements Serializable {

    public EventIdentifier {
        Objects.requireNonNull(value, "eventId value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("eventId value must not be blank");
        }
    }
}

package com.enterprise.event.core.validator;

import com.eventplatform.exception.EventPlatformExceptionContext;
import com.eventplatform.exception.EventValidationException;

import java.util.Objects;

public class DefaultEventValidator<T> implements EventValidator<T> {

    @Override
    public void validate(T payload) {
        Objects.requireNonNull(payload, "payload must not be null");

        if (payload instanceof String s && s.isBlank()) {
            throw new EventValidationException(
                    "Event payload cannot be blank",
                    new EventPlatformExceptionContext("unknown", "string", "unknown", "unknown", "unknown", "unknown", "unknown")
            );
        }
    }
}

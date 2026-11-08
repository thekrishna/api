package com.enterprise.event.core.registry;

import java.io.Serializable;

public record EventTypeDescriptor(
        String eventType,
        String version,
        String description,
        Class<?> payloadType
) implements Serializable {
}

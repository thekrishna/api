package com.eventplatform.exception;

import java.io.Serializable;

public record EventPlatformExceptionClassification(
        EventPlatformExceptionCategory category,
        EventPlatformExceptionAction action
) implements Serializable {
}

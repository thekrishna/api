package com.eventplatform.exception;

public class EventSerializationException extends EventPlatformException {

    public EventSerializationException(String message, EventPlatformExceptionContext context) {
        super(EventPlatformErrorCode.EVENT_SERIALIZATION_FAILED, message, context);
    }

    public EventSerializationException(String message, EventPlatformExceptionContext context, Throwable cause) {
        super(EventPlatformErrorCode.EVENT_SERIALIZATION_FAILED, message, context, cause);
    }
}

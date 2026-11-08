package com.eventplatform.exception;

public class EventValidationException extends EventPlatformException {

    public EventValidationException(String message, EventPlatformExceptionContext context) {
        super(EventPlatformErrorCode.EVENT_VALIDATION_FAILED, message, context);
    }

    public EventValidationException(String message, EventPlatformExceptionContext context, Throwable cause) {
        super(EventPlatformErrorCode.EVENT_VALIDATION_FAILED, message, context, cause);
    }
}

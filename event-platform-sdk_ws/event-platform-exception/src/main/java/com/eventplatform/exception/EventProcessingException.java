package com.eventplatform.exception;

public class EventProcessingException extends EventPlatformException {

    public EventProcessingException(String message, EventPlatformExceptionContext context) {
        super(EventPlatformErrorCode.EVENT_PROCESSING_FAILED, message, context);
    }

    public EventProcessingException(String message, EventPlatformExceptionContext context, Throwable cause) {
        super(EventPlatformErrorCode.EVENT_PROCESSING_FAILED, message, context, cause);
    }
}

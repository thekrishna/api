package com.eventplatform.exception;

public class EventConfigurationException extends EventPlatformException {

    public EventConfigurationException(String message, EventPlatformExceptionContext context) {
        super(EventPlatformErrorCode.EVENT_CONFIGURATION_INVALID, message, context);
    }

    public EventConfigurationException(String message, EventPlatformExceptionContext context, Throwable cause) {
        super(EventPlatformErrorCode.EVENT_CONFIGURATION_INVALID, message, context, cause);
    }
}

package com.eventplatform.exception;

public interface EventPlatformExceptionHandler {

    EventPlatformException handle(Throwable throwable, EventPlatformExceptionContext context);
}

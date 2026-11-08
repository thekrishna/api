package com.eventplatform.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultEventPlatformExceptionHandler implements EventPlatformExceptionHandler {

    @Override
    public EventPlatformException handle(Throwable throwable, EventPlatformExceptionContext context) {
        if (throwable instanceof EventPlatformException eventPlatformException) {
            logException(eventPlatformException, context);
            return eventPlatformException;
        }

        EventPlatformExceptionClassification classification = EventPlatformExceptionClassifier.classify(throwable);
        EventPlatformException exception = new EventPlatformException(
                EventPlatformErrorCode.EVENT_UNKNOWN_FAILURE,
                throwable.getMessage() == null ? "unknown platform failure" : throwable.getMessage(),
                context,
                throwable
        );

        logException(exception, context);
        return exception;
    }

    private void logException(EventPlatformException exception, EventPlatformExceptionContext context) {
        log.error(
                "event-platform exception detected: errorCode={}, category={}, action={}, eventId={}, eventType={}, aggregateId={}, correlationId={}, tenantId={}, traceId={}, source={}, message={}",
                exception.getErrorCode(),
                exception.getCategory(),
                exception.getAction(),
                context.eventId(),
                context.eventType(),
                context.aggregateId(),
                context.correlationId(),
                context.tenantId(),
                context.traceId(),
                context.source(),
                exception.getMessage()
        );
    }
}

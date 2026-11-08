package com.eventplatform.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventPlatformExceptionTest {

    @Test
    void shouldCreateTypedExceptionWithDefaultPolicy() {
        EventPlatformExceptionContext context = new EventPlatformExceptionContext(
                "evt-100",
                "order.created",
                "order-42",
                "corr-3",
                "tenant-1",
                "trace-88",
                "order-service"
        );

        EventPlatformException exception = EventPlatformExceptionFactory.processingFailure(
                "processing failed while writing to stream",
                context,
                new IllegalStateException("temporary failure")
        );

        assertEquals(EventPlatformErrorCode.EVENT_PROCESSING_FAILED, exception.getErrorCode());
        assertEquals(EventPlatformExceptionCategory.PROCESSING, exception.getCategory());
        assertEquals(EventPlatformExceptionAction.RETRY, exception.getAction());
        assertTrue(exception.isRetriable());
        assertEquals("evt-100", exception.getEventId());
        assertEquals("order.created", exception.getEventType());
    }

    @Test
    void shouldClassifyUnknownExceptions() {
        Throwable throwable = new RuntimeException("database timeout while reading from queue");

        EventPlatformExceptionClassification classification = EventPlatformExceptionClassifier.classify(throwable);

        assertEquals(EventPlatformExceptionCategory.RETRY, classification.category());
        assertEquals(EventPlatformExceptionAction.RETRY, classification.action());
    }

    @Test
    void shouldHandleExceptionViaDefaultHandler() {
        EventPlatformExceptionContext context = new EventPlatformExceptionContext(
                "evt-200",
                "user.created",
                "user-10",
                "corr-9",
                "tenant-9",
                "trace-77",
                "user-service"
        );

        EventPlatformExceptionHandler handler = new DefaultEventPlatformExceptionHandler();
        EventPlatformException result = handler.handle(new IllegalArgumentException("invalid event payload"), context);

        assertNotNull(result);
        assertEquals(EventPlatformErrorCode.EVENT_UNKNOWN_FAILURE, result.getErrorCode());
        assertEquals(EventPlatformExceptionAction.RETRY, result.getAction());
    }

    @Test
    void shouldCreateSpecificValidationException() {
        EventPlatformExceptionContext context = new EventPlatformExceptionContext(
                "evt-987",
                "customer.updated",
                "customer-1",
                "corr-54",
                "tenant-2",
                "trace-5",
                "customer-service"
        );

        EventValidationException exception = new EventValidationException("payload validation failed", context);

        assertEquals(EventPlatformExceptionCategory.VALIDATION, exception.getCategory());
        assertEquals(EventPlatformExceptionAction.IGNORE, exception.getAction());
        assertFalse(exception.isRetriable());
    }
}

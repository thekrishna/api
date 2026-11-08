package com.eventplatform.core;

import com.eventplatform.model.event.EventHeaders;
import com.eventplatform.model.event.EventMetadata;
import com.eventplatform.model.event.EventStatus;
import com.eventplatform.model.event.GenericEventEnvelope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class EventPlatformCoreTest {

    @Test
    void shouldCreateEnvelopeFromFactory() {
        EventProcessingContext context = new EventProcessingContext(
                "tenant-1",
                "trace-42",
                "corr-42",
                "order-service",
                "order-24",
                "order.created"
        );

        EventHeaders headers = EventHeaders.builder()
                .put("tenant-id", "tenant-1")
                .build();

        EventMetadata metadata = EventMetadata.builder()
                .aggregateId("order-24")
                .aggregateType("order")
                .eventType("order.created")
                .eventVersion("v1")
                .source("order-service")
                .occurredAt(Instant.now())
                .build();

        DefaultEventEnvelopeFactory factory = new DefaultEventEnvelopeFactory();
        GenericEventEnvelope<String> envelope = factory.create(
                "order.created",
                "order-service",
                EventStatus.NEW,
                "accepted",
                context,
                headers,
                metadata
        );

        assertNotNull(envelope.getEventId());
        assertEquals("order.created", envelope.getEventType());
        assertEquals("order-service", envelope.getSource());
        assertEquals("accepted", envelope.getPayload());
        assertEquals("tenant-1", envelope.getHeaders().get("tenant-id"));
    }

    @Test
    void shouldRegisterAndDispatchHandler() {
        DefaultEventHandlerRegistry registry = new DefaultEventHandlerRegistry();
        DefaultEventPublisher<String> publisher = new DefaultEventPublisher<>(registry);

        AtomicBoolean called = new AtomicBoolean(false);
        registry.register("invoice.created", envelope -> {
            called.set(true);
            assertEquals("invoice.created", envelope.getEventType());
        });

        GenericEventEnvelope<String> envelope = GenericEventEnvelope.<String>builder()
                .eventId(new com.eventplatform.model.event.EventIdentifier("evt-invoice"))
                .eventType("invoice.created")
                .eventTypeVersion(new com.eventplatform.model.event.EventTypeVersion("invoice.created", "v1"))
                .source("billing-service")
                .status(EventStatus.NEW)
                .occurredAt(Instant.now())
                .metadata(EventMetadata.builder()
                        .aggregateId("invoice-2")
                        .aggregateType("invoice")
                        .source("billing-service")
                        .eventType("invoice.created")
                        .eventVersion("v1")
                        .occurredAt(Instant.now())
                        .build())
                .headers(EventHeaders.builder().build())
                .payload("paid")
                .build();

        publisher.publish(envelope);

        assertTrue(called.get());
    }

    @Test
    void shouldExposeRegistryMetadata() {
        DefaultEventHandlerRegistry registry = new DefaultEventHandlerRegistry();
        registry.register("user.created", envelope -> {
        });

        assertTrue(registry.hasHandler("user.created"));
        assertNotNull(registry.getHandler("user.created"));
    }

}

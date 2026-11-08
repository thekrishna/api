package com.eventplatform.model;

import com.eventplatform.model.event.DomainEvent;
import com.eventplatform.model.event.EventIdentifier;
import com.eventplatform.model.event.EventMetadata;
import com.eventplatform.model.event.EventStatus;
import com.eventplatform.model.event.EventTypeVersion;
import com.eventplatform.model.event.GenericEventEnvelope;
import com.eventplatform.model.event.EventHeaders;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenericEventEnvelopeTest {

    @Test
    void shouldCreateGenericEnvelopeWithMetadataAndHeaders() {
        Instant now = Instant.parse("2026-11-08T12:00:00Z");

        var eventId = new EventIdentifier("evt-123");
        var headers = EventHeaders.builder()
                .put("tenant-id", "tenant-1")
                .put("trace-id", "trace-9")
                .build();

        var envelope = GenericEventEnvelope.<OrderCreatedEvent>builder()
                .eventId(eventId)
                .eventType("order.created")
                .eventTypeVersion(new EventTypeVersion("order.created", "v1"))
                .source("order-service")
                .status(EventStatus.NEW)
                .occurredAt(now)
                .payload(new OrderCreatedEvent("order-42", "customer-9"))
                .metadata(EventMetadata.builder()
                        .aggregateType("order")
                        .aggregateId("order-42")
                        .source("order-service")
                        .eventType("order.created")
                        .eventVersion("v1")
                        .occurredAt(now)
                        .build())
                .headers(headers)
                .build();

        assertEquals("evt-123", envelope.getEventId().value());
        assertEquals("order.created", envelope.getEventType());
        assertEquals("v1", envelope.getEventTypeVersion().version());
        assertEquals("order-42", envelope.getMetadata().aggregateId());
        assertEquals("tenant-1", envelope.getHeaders().get("tenant-id"));
        assertEquals("trace-9", envelope.getHeaders().get("trace-id"));
        assertEquals("order-42", envelope.getPayload().orderId());
        assertEquals(EventStatus.NEW, envelope.getStatus());
    }

    @Test
    void shouldAllowHeadersToBeCaseInsensitiveAndImmutable() {
        var headers = EventHeaders.builder()
                .put("tenant-id", "tenant-1")
                .build();

        assertEquals("tenant-1", headers.get("tenant-id"));
        assertEquals("tenant-1", headers.get("Tenant-Id"));
        assertTrue(headers.contains("tenant-id"));
        assertEquals(Map.of("tenant-id", "tenant-1"), headers.toMap());

        assertThrows(UnsupportedOperationException.class, () -> headers.toMap().put("x", "y"));
    }

    @Test
    void shouldExposeDomainEventContract() {
        var event = new OrderCreatedEvent("order-88", "customer-77");

        assertEquals("order.created", event.eventType());
        assertEquals("order", event.aggregateType());
        assertEquals("order-88", event.aggregateId());
        assertNotNull(event.occurredAt());
    }

    private record OrderCreatedEvent(String orderId, String customerId) implements DomainEvent<OrderCreatedEvent> {
        @Override
        public String eventType() {
            return "order.created";
        }

        @Override
        public String aggregateType() {
            return "order";
        }

        @Override
        public String aggregateId() {
            return orderId;
        }

        @Override
        public Instant occurredAt() {
            return Instant.now();
        }

        @Override
        public OrderCreatedEvent payload() {
            return this;
        }
    }
}

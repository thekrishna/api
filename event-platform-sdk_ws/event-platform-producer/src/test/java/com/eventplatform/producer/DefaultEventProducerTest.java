package com.eventplatform.producer;

import com.eventplatform.core.DefaultEventEnvelopeFactory;
import com.eventplatform.core.EventProcessingContext;
import com.eventplatform.model.event.EventHeaders;
import com.eventplatform.model.event.EventMetadata;
import com.eventplatform.model.event.EventStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEventProducerTest {

    @Test
    void shouldCreateEnvelopeAndPublish() {
        DefaultEventProducer<String> producer = new DefaultEventProducer<>();
        EventProcessingContext context = new EventProcessingContext("tenant-1", "trace-1", "corr-1", "order-service", "order-11", "order.created");

        var envelope = producer.createEnvelope(
                "order.created",
                "order-service",
                EventStatus.NEW,
                "payload",
                context,
                EventHeaders.builder().put("tenant-id", "tenant-1").build(),
                EventMetadata.builder().aggregateId("order-11").aggregateType("order").eventType("order.created").eventVersion("v1").source("order-service").build()
        );

        assertEquals("order.created", envelope.getEventType());
        assertEquals("payload", envelope.getPayload());

        assertDoesNotThrow(() -> producer.publish(envelope));
    }
}

package com.eventplatform.consumer;

import com.eventplatform.model.event.GenericEventEnvelope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEventConsumerTest {

    @Test
    void shouldConsumeEnvelope() {
        AtomicBoolean invoked = new AtomicBoolean(false);
        DefaultEventConsumer<String> consumer = new DefaultEventConsumer<>(envelope -> {
            invoked.set(true);
            assertEquals("order.created", envelope.getEventType());
        });

        consumer.consume(GenericEventEnvelope.<String>builder()
                .eventId(new com.eventplatform.model.event.EventIdentifier("evt-1"))
                .eventType("order.created")
                .eventTypeVersion(new com.eventplatform.model.event.EventTypeVersion("order.created", "v1"))
                .source("order-service")
                .status(com.eventplatform.model.event.EventStatus.NEW)
                .payload("test")
                .build());

        assertTrue(invoked.get());
    }

    private static final class AtomicBoolean {
        private boolean value;

        private AtomicBoolean(boolean value) {
            this.value = value;
        }

        void set(boolean value) {
            this.value = value;
        }

        boolean get() {
            return value;
        }
    }
}

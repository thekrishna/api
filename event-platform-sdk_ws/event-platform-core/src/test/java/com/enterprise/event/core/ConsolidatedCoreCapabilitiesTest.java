package com.enterprise.event.core;

import com.enterprise.event.core.aggregation.DefaultEventAggregator;
import com.enterprise.event.core.dlq.DeadLetterRecord;
import com.enterprise.event.core.dlq.InMemoryDeadLetterQueue;
import com.enterprise.event.core.inbox.InboxEntry;
import com.enterprise.event.core.inbox.InMemoryInboxStore;
import com.enterprise.event.core.ordering.AggregateOrderingService;
import com.enterprise.event.core.outbox.InMemoryOutboxStore;
import com.enterprise.event.core.outbox.OutboxEntry;
import com.enterprise.event.core.pending.InMemoryPendingEventStore;
import com.enterprise.event.core.pending.PendingEvent;
import com.enterprise.event.core.registry.DefaultEventTypeRegistry;
import com.enterprise.event.core.registry.EventTypeDescriptor;
import com.enterprise.event.core.retry.RetryDecision;
import com.enterprise.event.core.retry.RetryPolicy;
import com.enterprise.event.core.retry.RetryPolicyService;
import com.enterprise.event.core.serializer.JsonEventSerializer;
import com.enterprise.event.core.state.EventLifecycleState;
import com.enterprise.event.core.state.EventStateRecord;
import com.enterprise.event.core.state.InMemoryEventStateStore;
import com.enterprise.event.core.validator.DefaultEventValidator;
import com.eventplatform.model.event.EventHeaders;
import com.eventplatform.model.event.EventIdentifier;
import com.eventplatform.model.event.EventMetadata;
import com.eventplatform.model.event.EventStatus;
import com.eventplatform.model.event.EventTypeVersion;
import com.eventplatform.model.event.GenericEventEnvelope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolidatedCoreCapabilitiesTest {

    @Test
    void serializerShouldRoundTripPayload() {
        JsonEventSerializer<TestPayload> serializer = new JsonEventSerializer<>();
        TestPayload payload = new TestPayload("evt-1", "order.created");

        String json = serializer.serialize(payload);
        TestPayload restored = serializer.deserialize(json, TestPayload.class);

        assertNotNull(json);
        assertEquals(payload.id(), restored.id());
        assertEquals(payload.type(), restored.type());
    }

    @Test
    void validatorShouldRejectBlankPayload() {
        DefaultEventValidator<String> validator = new DefaultEventValidator<>();

        assertThrows(NullPointerException.class, () -> validator.validate(null));
        assertThrows(RuntimeException.class, () -> validator.validate("   "));
    }

    @Test
    void outboxShouldTrackPendingEntries() {
        InMemoryOutboxStore store = new InMemoryOutboxStore();
        OutboxEntry entry = new OutboxEntry("outbox-1", "user.created", "{}", Instant.now(), "PENDING");

        store.save(entry);

        assertEquals(1, store.pending().size());
        assertEquals("outbox-1", store.findById("outbox-1").orElseThrow().id());
    }

    @Test
    void inboxShouldDeduplicateEvents() {
        InMemoryInboxStore store = new InMemoryInboxStore();
        InboxEntry entry = new InboxEntry("evt-1", "user.created", "consumer-1", Instant.now(), "RECEIVED");

        store.save(entry);

        assertEquals(1, store.all().size());
        assertTrue(store.findById("evt-1").isPresent());
    }

    @Test
    void orderingServiceShouldSortByTime() {
        AggregateOrderingService service = new AggregateOrderingService();
        GenericEventEnvelope<String> first = envelope("a-1", "order.created", 1000L, "first", "evt-first");
        GenericEventEnvelope<String> second = envelope("a-1", "order.created", 2000L, "second", "evt-second");

        service.register(first);
        service.register(second);

        List<GenericEventEnvelope<?>> ordered = service.orderedFor("a-1");
        assertEquals(2, ordered.size());
        assertEquals("first", ordered.get(0).getPayload());
    }

    @Test
    void aggregatorShouldCorrelateEvents() {
        DefaultEventAggregator aggregator = new DefaultEventAggregator();
        GenericEventEnvelope<String> first = envelope("corr-1", "user.created", 1000L, "alpha", "evt-alpha");
        GenericEventEnvelope<String> second = envelope("corr-1", "user.updated", 2000L, "beta", "evt-beta");

        var result = aggregator.aggregate("corr-1", List.of(first, second));

        assertEquals("corr-1", result.correlationId());
        assertEquals(2, result.aggregates().size());
        assertTrue(result.aggregates().containsKey(first.getEventId().value()));
    }

    @Test
    void registryShouldRegisterAndLookupDescriptor() {
        DefaultEventTypeRegistry registry = new DefaultEventTypeRegistry();

        registry.register(new EventTypeDescriptor("user.created", "v1", "user created", String.class), envelope -> {
        });

        assertTrue(registry.contains("user.created"));
        assertEquals("v1", registry.find("user.created").version());
        assertNotNull(registry.handler("user.created"));
    }

    @Test
    void retryPolicyServiceShouldDecideRetryOrDlq() {
        RetryPolicyService service = new RetryPolicyService(RetryPolicy.defaults());

        assertEquals(RetryDecision.RETRY, service.decide(2));
        assertEquals(RetryDecision.DLQ, service.decide(5));
    }

    @Test
    void pendingStoreShouldTrackRetries() {
        InMemoryPendingEventStore store = new InMemoryPendingEventStore();
        PendingEvent event = new PendingEvent("evt-10", "user.created", 1, Instant.now(), "PENDING");

        store.save(event);

        assertTrue(store.find("evt-10").isPresent());
        assertFalse(store.all().isEmpty());
    }

    @Test
    void dlqShouldStoreFailedEvents() {
        InMemoryDeadLetterQueue queue = new InMemoryDeadLetterQueue();
        DeadLetterRecord record = new DeadLetterRecord("evt-99", "user.created", "E001", "invalid payload", Instant.now());

        queue.enqueue(record);

        assertEquals(1, queue.all().size());
        assertEquals("evt-99", queue.find("evt-99").orElseThrow().eventId());
    }

    @Test
    void stateStoreShouldTrackLifecycle() {
        InMemoryEventStateStore store = new InMemoryEventStateStore();
        EventStateRecord record = new EventStateRecord("evt-7", EventLifecycleState.PENDING_RETRY, Instant.now());

        store.save(record);

        assertEquals(EventLifecycleState.PENDING_RETRY, store.find("evt-7").orElseThrow().state());
    }

    private GenericEventEnvelope<String> envelope(String aggregateId, String eventType, long epochMilli, String payload, String eventIdValue) {
        return GenericEventEnvelope.<String>builder()
                .eventId(new EventIdentifier(eventIdValue))
                .eventType(eventType)
                .eventTypeVersion(new EventTypeVersion(eventType, "v1"))
                .source("order-service")
                .status(EventStatus.NEW)
                .occurredAt(Instant.ofEpochMilli(epochMilli))
                .metadata(EventMetadata.builder()
                        .eventType(eventType)
                        .eventVersion("v1")
                        .source("order-service")
                        .aggregateType("order")
                        .aggregateId(aggregateId)
                        .occurredAt(Instant.ofEpochMilli(epochMilli))
                        .schemaVersion("v1")
                        .description("test event")
                        .build())
                .headers(EventHeaders.builder().put("tenant-id", "tenant-1").build())
                .payload(payload)
                .build();
    }

    private record TestPayload(String id, String type) {
    }
}

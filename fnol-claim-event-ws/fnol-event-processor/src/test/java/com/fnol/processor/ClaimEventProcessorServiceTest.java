package com.fnol.processor;

import com.fnol.common.ClaimEventEnvelope;
import com.fnol.common.ClaimStatus;
import com.fnol.common.EventType;
import com.fnol.processor.service.ClaimEventProcessorService;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ClaimEventProcessorServiceTest {

    @Test
    void shouldProcessSubmittedEvent() {
        ClaimEventProcessorService service = new ClaimEventProcessorService();
        ClaimEventEnvelope<String> event = new ClaimEventEnvelope<>(
                "evt-1",
                EventType.CLAIM_SUBMITTED,
                "1.0",
                "CLM-1",
                Instant.now(),
                "fnol-api-service",
                "submitted"
        );

        String result = service.processEvent(event);

        assertEquals("processed", result);
        assertEquals(ClaimStatus.VALIDATED, service.getCurrentStatus("CLM-1"));
    }
}

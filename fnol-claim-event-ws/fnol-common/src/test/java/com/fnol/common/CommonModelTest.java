package com.fnol.common;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CommonModelTest {

    @Test
    void shouldCreateClaimSubmissionRequest() {
        ClaimSubmissionRequest request = new ClaimSubmissionRequest(
                "POL-1001",
                "CUS-2001",
                "Jane Doe",
                "2026-11-08",
                "Austin, TX",
                ClaimType.PROPERTY,
                "Roof damage from hail",
                new BigDecimal("15400.00")
        );

        assertEquals("POL-1001", request.policyNumber());
        assertEquals(ClaimType.PROPERTY, request.claimType());
        assertEquals(new BigDecimal("15400.00"), request.claimAmount());
    }

    @Test
    void shouldBuildClaimEventEnvelope() {
        ClaimEventEnvelope<String> envelope = new ClaimEventEnvelope<>(
                "evt-1",
                EventType.CLAIM_SUBMITTED,
                "1.0",
                "CLM-1",
                java.time.Instant.now(),
                "fnol-api-service",
                "payload"
        );

        assertEquals("CLM-1", envelope.claimId());
        assertEquals(EventType.CLAIM_SUBMITTED, envelope.eventType());
    }
}

package com.fnol.api;

import com.fnol.api.config.KinesisProperties;
import com.fnol.api.service.ClaimService;
import com.fnol.api.service.KinesisClaimPublisher;
import com.fnol.common.ClaimStatus;
import com.fnol.common.ClaimSubmissionRequest;
import com.fnol.common.ClaimType;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ClaimServiceTest {

    @Test
    void shouldCreateClaimAndPublishEvent() {
        KinesisClient kinesisClient = mock(KinesisClient.class);
        KinesisClaimPublisher publisher = new KinesisClaimPublisher(kinesisClient);
        ClaimService service = new ClaimService(publisher, new KinesisProperties());

        ClaimSubmissionRequest request = new ClaimSubmissionRequest(
                "POL-1001",
                "CUS-2001",
                "Jane Doe",
                "2026-11-08",
                "Austin, TX",
                ClaimType.PROPERTY,
                "Roof damage from hail",
                new BigDecimal("15000.00")
        );

        var claim = service.createClaim(request);

        assertNotNull(claim.claimId());
        assertEquals(ClaimStatus.SUBMITTED, claim.claimStatus());
        assertEquals(1, service.getClaimEvents(claim.claimId()).size());
        verify(kinesisClient).putRecord(any(PutRecordRequest.class));
    }
}

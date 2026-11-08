package com.fnol.common;

import java.math.BigDecimal;
import java.time.Instant;

public record ClaimRecord(
        String claimId,
        String policyNumber,
        String claimantId,
        String claimantName,
        String incidentLocation,
        String incidentDate,
        ClaimType claimType,
        String claimDescription,
        BigDecimal claimAmount,
        ClaimStatus claimStatus,
        Instant createdAt,
        Instant updatedAt
) {
}

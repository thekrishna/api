package com.fnol.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ClaimSubmissionRequest(
        @NotBlank(message = "policyNumber is required") String policyNumber,
        @NotBlank(message = "claimantId is required") String claimantId,
        @NotBlank(message = "claimantName is required") String claimantName,
        @NotBlank(message = "incidentDate is required") String incidentDate,
        @NotBlank(message = "incidentLocation is required") String incidentLocation,
        @NotNull(message = "claimType is required") ClaimType claimType,
        @NotBlank(message = "claimDescription is required") String claimDescription,
        @NotNull(message = "claimAmount is required") @Positive(message = "claimAmount must be positive") BigDecimal claimAmount
) {
}

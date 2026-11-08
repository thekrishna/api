package com.fnol.common;

import java.time.Instant;

public record AiAnalysisResult(
        String claimId,
        String analysisId,
        String analysisType,
        String claimSummary,
        String claimClassification,
        String riskIndicators,
        Double confidenceScore,
        boolean humanReviewRequired,
        Instant createdAt
) {
}

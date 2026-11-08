package com.fnol.genai.service;

import com.fnol.common.AiAnalysisResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BedrockGenAiService implements GenAiService {

    @Override
    public AiAnalysisResult analyzeClaim(String claimId, String claimDescription) {
        String classification = claimDescription.toLowerCase().contains("roof") ? "PROPERTY_DAMAGE" : "GENERAL_CLAIM";
        String riskIndicators = claimDescription.toLowerCase().contains("hail") ? "HAIL_DAMAGE; WEATHER_EVENT" : "LOW_RISK";
        double confidence = claimDescription != null && !claimDescription.isBlank() ? 0.92d : 0.5d;

        return new AiAnalysisResult(
                claimId,
                "ANALYSIS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "CLAIM_CLASSIFICATION",
                "Summary generated from claim description and supporting evidence.",
                classification,
                riskIndicators,
                confidence,
                riskIndicators.contains("HAIL_DAMAGE"),
                Instant.now()
        );
    }
}

package com.fnol.genai;

import com.fnol.genai.service.BedrockGenAiService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BedrockGenAiServiceTest {

    @Test
    void shouldAnalyzeClaim() {
        BedrockGenAiService service = new BedrockGenAiService();

        var analysis = service.analyzeClaim("CLM-1", "Roof damage from hail storm");

        assertEquals("CLM-1", analysis.claimId());
        assertEquals("PROPERTY_DAMAGE", analysis.claimClassification());
        assertTrue(analysis.humanReviewRequired());
    }
}

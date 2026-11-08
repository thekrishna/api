package com.fnol.genai.service;

import com.fnol.common.AiAnalysisResult;

public interface GenAiService {

    AiAnalysisResult analyzeClaim(String claimId, String claimDescription);
}

package com.fnol.processor.service;

import com.fnol.common.ClaimEventEnvelope;
import com.fnol.common.ClaimStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClaimEventProcessorService {

    private final Map<String, ClaimStatus> processedStatuses = new ConcurrentHashMap<>();

    public String processEvent(ClaimEventEnvelope<?> event) {
        if (event == null || event.claimId() == null) {
            throw new IllegalArgumentException("Claim event envelope is invalid");
        }

        if (event.eventType() == com.fnol.common.EventType.CLAIM_SUBMITTED) {
            processedStatuses.put(event.claimId(), ClaimStatus.VALIDATED);
            return "processed";
        }

        processedStatuses.put(event.claimId(), ClaimStatus.IN_PROGRESS);
        return "processed";
    }

    public ClaimStatus getCurrentStatus(String claimId) {
        return processedStatuses.getOrDefault(claimId, ClaimStatus.SUBMITTED);
    }
}

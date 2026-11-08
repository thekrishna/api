package com.fnol.api.service;

import com.fnol.api.config.KinesisProperties;
import com.fnol.common.ClaimEventEnvelope;
import com.fnol.common.ClaimRecord;
import com.fnol.common.ClaimStatus;
import com.fnol.common.ClaimSubmissionRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClaimService {

    private final ConcurrentHashMap<String, ClaimRecord> claims = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<ClaimEventEnvelope<?>>> claimEvents = new ConcurrentHashMap<>();
    private final KinesisClaimPublisher kinesisClaimPublisher;
    private final KinesisProperties kinesisProperties;

    public ClaimService(KinesisClaimPublisher kinesisClaimPublisher, KinesisProperties kinesisProperties) {
        this.kinesisClaimPublisher = kinesisClaimPublisher;
        this.kinesisProperties = kinesisProperties;
    }

    public ClaimRecord createClaim(ClaimSubmissionRequest request) {
        String claimId = "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Instant now = Instant.now();
        ClaimRecord claim = new ClaimRecord(
                claimId,
                request.policyNumber(),
                request.claimantId(),
                request.claimantName(),
                request.incidentLocation(),
                request.incidentDate(),
                request.claimType(),
                request.claimDescription(),
                request.claimAmount(),
                ClaimStatus.SUBMITTED,
                now,
                now
        );
        claims.put(claimId, claim);

        ClaimEventEnvelope<ClaimRecord> event = new ClaimEventEnvelope<>(
                UUID.randomUUID().toString(),
                com.fnol.common.EventType.CLAIM_SUBMITTED,
                "1.0",
                claimId,
                now,
                "fnol-api-service",
                claim
        );
        claimEvents.computeIfAbsent(claimId, key -> new ArrayList<>()).add(event);
        kinesisClaimPublisher.publish(event, kinesisProperties.getFnolClaimEventsStream());
        return claim;
    }

    public ClaimRecord getClaim(String claimId) {
        return claims.get(claimId);
    }

    public List<ClaimRecord> getClaims(int page, int size) {
        return claims.values().stream()
                .sorted(Comparator.comparing(ClaimRecord::createdAt).reversed())
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    public List<ClaimEventEnvelope<?>> getClaimEvents(String claimId) {
        return claimEvents.getOrDefault(claimId, List.of());
    }

    public ClaimStatus getStatus(String claimId) {
        ClaimRecord claim = claims.get(claimId);
        return claim == null ? null : claim.claimStatus();
    }

    public ClaimRecord updateStatus(String claimId, ClaimStatus status) {
        ClaimRecord existing = claims.get(claimId);
        if (existing == null) {
            throw new IllegalArgumentException("Claim not found: " + claimId);
        }

        ClaimRecord updated = new ClaimRecord(
                existing.claimId(),
                existing.policyNumber(),
                existing.claimantId(),
                existing.claimantName(),
                existing.incidentLocation(),
                existing.incidentDate(),
                existing.claimType(),
                existing.claimDescription(),
                existing.claimAmount(),
                status,
                existing.createdAt(),
                Instant.now()
        );
        claims.put(claimId, updated);

        ClaimEventEnvelope<String> event = new ClaimEventEnvelope<>(
                UUID.randomUUID().toString(),
                com.fnol.common.EventType.CLAIM_STATUS_UPDATED,
                "1.0",
                claimId,
                Instant.now(),
                "fnol-api-service",
                status.name()
        );
        claimEvents.computeIfAbsent(claimId, key -> new ArrayList<>()).add(event);
        return updated;
    }

    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new java.util.HashMap<>();
        summary.put("totalClaims", (long) claims.size());
        summary.put("submitted", claims.values().stream().filter(c -> c.claimStatus() == ClaimStatus.SUBMITTED).count());
        summary.put("inProgress", claims.values().stream().filter(c -> c.claimStatus() == ClaimStatus.IN_PROGRESS).count());
        summary.put("paid", claims.values().stream().filter(c -> c.claimStatus() == ClaimStatus.PAID).count());
        summary.put("settled", claims.values().stream().filter(c -> c.claimStatus() == ClaimStatus.SETTLED).count());
        return summary;
    }

    public BigDecimal getTotalClaimVolume() {
        return claims.values().stream()
                .map(ClaimRecord::claimAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.fnol.api.controller;

import com.fnol.api.service.ClaimService;
import com.fnol.common.ClaimEventEnvelope;
import com.fnol.common.ClaimRecord;
import com.fnol.common.ClaimStatus;
import com.fnol.common.ClaimSubmissionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/claims")
    public ResponseEntity<ClaimRecord> submitClaim(@Valid @RequestBody ClaimSubmissionRequest request) {
        return ResponseEntity.ok(claimService.createClaim(request));
    }

    @GetMapping("/claims/{claimId}")
    public ResponseEntity<ClaimRecord> getClaim(@PathVariable String claimId) {
        ClaimRecord claim = claimService.getClaim(claimId);
        if (claim == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(claim);
    }

    @GetMapping("/claims")
    public ResponseEntity<List<ClaimRecord>> getClaims(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(claimService.getClaims(page, size));
    }

    @GetMapping("/claims/{claimId}/events")
    public ResponseEntity<List<ClaimEventEnvelope<?>>> getClaimEvents(@PathVariable String claimId) {
        return ResponseEntity.ok(claimService.getClaimEvents(claimId));
    }

    @GetMapping("/claims/{claimId}/status")
    public ResponseEntity<ClaimStatus> getClaimStatus(@PathVariable String claimId) {
        ClaimStatus status = claimService.getStatus(claimId);
        return status == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(status);
    }

    @PatchMapping("/claims/{claimId}/status")
    public ResponseEntity<ClaimRecord> updateClaimStatus(
            @PathVariable String claimId,
            @RequestBody Map<String, String> statusBody) {
        String status = statusBody.get("status");
        return ResponseEntity.ok(claimService.updateStatus(claimId, ClaimStatus.valueOf(status)));
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<Map<String, Long>> summary() {
        return ResponseEntity.ok(claimService.getDashboardSummary());
    }
}

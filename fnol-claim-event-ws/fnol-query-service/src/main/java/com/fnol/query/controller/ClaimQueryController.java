package com.fnol.query.controller;

import com.fnol.common.ClaimRecord;
import com.fnol.query.service.ClaimQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ClaimQueryController {

    private final ClaimQueryService claimQueryService;

    public ClaimQueryController(ClaimQueryService claimQueryService) {
        this.claimQueryService = claimQueryService;
    }

    @GetMapping("/claims/{claimId}")
    public ResponseEntity<ClaimRecord> getClaim(@PathVariable String claimId) {
        ClaimRecord claim = claimQueryService.findById(claimId);
        return claim == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(claim);
    }

    @GetMapping("/claims")
    public ResponseEntity<List<ClaimRecord>> getClaims() {
        return ResponseEntity.ok(claimQueryService.findAll());
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(claimQueryService.dashboardSummary());
    }
}

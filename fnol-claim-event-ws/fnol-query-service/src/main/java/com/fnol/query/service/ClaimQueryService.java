package com.fnol.query.service;

import com.fnol.common.ClaimRecord;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClaimQueryService {

    private final Map<String, ClaimRecord> claims = new ConcurrentHashMap<>();

    public void saveClaim(ClaimRecord claim) {
        claims.put(claim.claimId(), claim);
    }

    public ClaimRecord findById(String claimId) {
        return claims.get(claimId);
    }

    public List<ClaimRecord> findAll() {
        return claims.values().stream().toList();
    }

    public Map<String, Object> dashboardSummary() {
        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalClaims", claims.size());
        summary.put("totalVolume", claims.values().stream().map(ClaimRecord::claimAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        return summary;
    }
}

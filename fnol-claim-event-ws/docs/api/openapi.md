# API Reference

## POST /api/v1/claims
Creates a new FNOL claim record and emits a claim event to Kinesis.

## GET /api/v1/claims/{claimId}
Retrieves a single claim record.

## GET /api/v1/claims
Retrieves claims with pagination.

## GET /api/v1/claims/{claimId}/events
Gets the event timeline for a claim.

## PATCH /api/v1/claims/{claimId}/status
Updates the claim lifecycle status.

## GET /api/v1/dashboard/summary
Returns the aggregate summary used by the UI.

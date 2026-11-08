# Event Schemas

## Claim submitted

```json
{
  "eventId": "uuid",
  "eventType": "CLAIM_SUBMITTED",
  "eventVersion": "1.0",
  "claimId": "CLM-1001",
  "timestamp": "2026-11-08T12:00:00Z",
  "source": "fnol-api-service",
  "payload": {
    "policyNumber": "POL-9001",
    "claimType": "PROPERTY",
    "claimStatus": "SUBMITTED"
  }
}
```

## Document processed

```json
{
  "eventId": "uuid",
  "eventType": "DOCUMENT_PROCESSED",
  "eventVersion": "1.0",
  "claimId": "CLM-1001",
  "timestamp": "2026-11-08T13:00:00Z",
  "source": "fnol-document-service",
  "payload": {
    "documentId": "DOC-1001",
    "processingStatus": "COMPLETED"
  }
}
```

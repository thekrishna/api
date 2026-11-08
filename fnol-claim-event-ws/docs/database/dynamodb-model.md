# DynamoDB Data Model

## `fnol-claims`
- Partition key: `claimId`
- Attributes: `policyNumber`, `claimantId`, `claimantName`, `incidentDate`, `incidentLocation`, `claimType`, `claimDescription`, `claimAmount`, `claimStatus`, `createdAt`, `updatedAt`
- Recommended GSIs: `policyNumber`, `claimantId`, `claimStatus`

## `fnol-claim-events`
- Key: `claimId` + `eventTimestampEventId`
- Stores event lifecycle and processing status.

## `fnol-documents`
- Key: `claimId` + `documentId`
- Stores metadata and upload status.

## `fnol-processing-status`
- Partition key: `claimId`
- Tracks workflow progress and retry counts.

## `fnol-ai-analysis`
- Key: `claimId` + `analysisId`
- Stores AI classification and risk indicators.

## `fnol-processed-events`
- Partition key: `eventId`
- Supports idempotent event processing.

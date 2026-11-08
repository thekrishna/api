# DynamoDB Initialization Guide

This directory contains scripts to create and verify the six DynamoDB tables used by the FNOL platform.

## Tables

1. `fnol-claims`
   - Partition key: `claimId`
   - GSI: `policyNumber`, `claimantId`, `claimStatus`
2. `fnol-claim-events`
   - Partition key: `claimId`
   - Sort key: `eventTimestampEventId`
3. `fnol-documents`
   - Partition key: `claimId`
   - Sort key: `documentId`
4. `fnol-processing-status`
   - Partition key: `claimId`
5. `fnol-ai-analysis`
   - Partition key: `claimId`
   - Sort key: `analysisId`
6. `fnol-processed-events`
   - Partition key: `eventId`

## LocalStack usage

```bash
export AWS_ENDPOINT_URL=http://localhost:4566
export AWS_REGION=us-east-1
./create-tables.sh
./seed-data.sh
./verify-tables.sh
```

## AWS usage

```bash
export AWS_PROFILE=my-profile
export AWS_REGION=us-east-1
./create-tables.sh
```

## PowerShell usage

```powershell
$env:AWS_ENDPOINT_URL='http://localhost:4566'
$env:AWS_REGION='us-east-1'
./create-tables.ps1
./seed-data.ps1
```

## Notes

- Scripts are safe to rerun.
- Billing mode uses PAY_PER_REQUEST for local development and cost efficiency.
- Use IAM roles in production and avoid hardcoding credentials.

# Terraform Resource Mapping for Existing FNOL Implementation

This document connects the current FNOL application implementation to the AWS resources provisioned by Terraform.

## 1. Application to infrastructure mapping

| Existing component | Current implementation signal | Terraform resource | Notes |
| --- | --- | --- | --- |
| FNOL API Service | publishes claim events through AWS Kinesis client | `aws_kinesis_stream` for `fnol-claim-events` | Mirrors `KinesisClaimPublisher` behavior |
| Document Service | emits document events and stores metadata | `aws_kinesis_stream` for `fnol-document-events` + `fnol-documents` table | Aligns with LocalStack scripts |
| Event Processor | updates status and tracking metadata | `aws_dynamodb_table` for `fnol-processing-status` and `fnol-claim-events` | In-memory processor in repo is a skeleton; this Terraform config matches the intended event flow |
| GenAI Service | stores analysis metadata | `aws_dynamodb_table` for `fnol-ai-analysis` | Used for claim risk analysis |
| Query Service | reads claim state and dashboard summaries | `aws_dynamodb_table` for `fnol-claims` + GSIs | Used for read-heavy queries |
| LocalStack init scripts | `infrastructure/dynamodb/create-tables.sh` and `infrastructure/kinesis/create-streams.sh` | Terraform equivalents | LocalStack remains the development source of truth for local runs |

## 2. Actual Kinesis streams from repository

The LocalStack scripts and project documentation define these Kinesis streams as the real application contract:

- `fnol-claim-events`
- `fnol-document-events`
- `fnol-processing-events`

The Terraform config provisions the same stream names by default and keeps them configurable via `var.kinesis_stream_names`.

## 3. Actual DynamoDB tables from repository

The LocalStack table creation script defines the following outputs as the operational schema:

- `fnol-claims`
- `fnol-claim-events`
- `fnol-documents`
- `fnol-processing-status`
- `fnol-ai-analysis`
- `fnol-processed-events`

The keys are:

- `fnol-claims`: `claimId` (hash)
- `fnol-claim-events`: `claimId` (hash), `eventTimestampEventId` (range)
- `fnol-documents`: `claimId` (hash), `documentId` (range)
- `fnol-processing-status`: `claimId` (hash)
- `fnol-ai-analysis`: `claimId` (hash), `analysisId` (range)
- `fnol-processed-events`: `eventId` (hash)

## 4. Global secondary indexes

The repository and data model docs suggest the following read patterns, so the Terraform config includes the corresponding GSIs:

- `fnol-claims`
  - `policyNumber-index` on `policyNumber`
  - `claimantId-index` on `claimantId`
  - `claimStatus-index` on `claimStatus`
- `fnol-processing-status`
  - `currentStatus-index` on `currentStatus`

No additional GSIs were added beyond the access patterns implied by the application model and docs.

## 5. Kinesis producer and consumer considerations

The current FNOL implementation includes a Kinesis producer (`KinesisClaimPublisher`) and a Kinesis client configuration, but it does not implement the Kinesis Client Library or a full checkpointing lease table. For that reason:

- the Terraform configuration includes stream provisioning
- the IAM role includes producer permissions
- checkpointing infrastructure is omitted unless the application adds a KCL-based consumer
- an optional DLQ is implemented as an environment switch rather than assumed requirement

## 6. IAM mapping

The application role is mapped to the following required permissions:

- Kinesis producer: `PutRecord`, `PutRecords`, `DescribeStreamSummary`, `ListShards`
- DynamoDB access: `GetItem`, `PutItem`, `UpdateItem`, `DeleteItem`, `Query`, `Scan`, `BatchGetItem`, `BatchWriteItem`, `DescribeTable`

## 7. LocalStack and AWS separation

The LocalStack setup in `docker-compose.yml` and the shell scripts are the source of truth for local development. The Terraform configuration supports a separate `use_localstack` path, but it does not automatically replace or override the shell-based local initialization scripts. That separation keeps local development and AWS deployment responsibilities clear.

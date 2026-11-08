# DynamoDB Table Ownership and Access

## Executive finding

The repository defines six DynamoDB table schemas in LocalStack scripts and Terraform, but the Java services do not currently read or write any of them. There is no source-code-confirmed service owner, reader, or writer for a table.

`fnol-api-service` declares the SDK v2 `dynamodb` dependency and creates a `DynamoDbClient` bean in `AwsConfiguration.dynamoDbClient`, but no class injects the bean or calls a DynamoDB operation. `fnol-document-service` and `fnol-event-processor` also declare the DynamoDB dependency without any Java DynamoDB call sites. `fnol-common`, `fnol-genai-service`, and `fnol-query-service` do not declare the SDK DynamoDB module.

The table definitions below are infrastructure contracts. They should not be confused with data actually persisted by the current application.

## Table inventory and source-backed access

| Table | Partition key | Sort key | GSIs (Terraform) | TTL | DynamoDB Streams | Java owner / readers / writers |
|---|---|---|---|---|---|---|
| `fnol-claims` | `claimId` (S) | None | `policyNumber-index` (`policyNumber`), `claimantId-index` (`claimantId`), `claimStatus-index` (`claimStatus`) | Disabled | Disabled | None implemented |
| `fnol-claim-events` | `claimId` (S) | `eventTimestampEventId` (S) | None | Disabled | Disabled | None implemented |
| `fnol-documents` | `claimId` (S) | `documentId` (S) | None | Disabled | Disabled | None implemented |
| `fnol-processing-status` | `claimId` (S) | None | `currentStatus-index` (`currentStatus`) | Disabled | Disabled | None implemented |
| `fnol-ai-analysis` | `claimId` (S) | `analysisId` (S) | None | Disabled | Disabled | None implemented |
| `fnol-processed-events` | `eventId` (S) | None | None | Disabled | Disabled | None implemented |

There are no local secondary indexes, LSIs, or table stream configurations in `infrastructure/docker/localstack/init-aws.sh` or `infrastructure/dynamodb/create-tables.sh`; those scripts create only the base keys. Terraform includes the listed GSIs for `fnol-claims` and `fnol-processing-status`. No LSI definition was found.

Terraform's default table objects disable TTL and DynamoDB Streams. The environment definitions do not turn either on; the DynamoDB module defaults a missing stream flag to false and only creates a TTL block when `ttl_enabled=true`.

## Table-by-table interpretation

| Table | Intended purpose reflected by schema/docs | What Java actually does |
|---|---|---|
| `fnol-claims` | Claim record keyed by claim ID | `fnol-api-service` stores `ClaimRecord` values in `ClaimService.claims`, a process-local `ConcurrentHashMap`; it does not persist to this table. Query service has a separate process-local map and no loader/writer. |
| `fnol-claim-events` | Event history keyed by claim and event key | Claim events are held in `ClaimService.claimEvents`; Kinesis publication is a separate stream operation. The table is not accessed. |
| `fnol-documents` | Claim/document metadata | `DocumentProcessingService` keeps document records in a process-local map; it does not use DynamoDB or publish a document event. |
| `fnol-processing-status` | Workflow status | `ClaimEventProcessorService` keeps status in a process-local map; it does not access this table. |
| `fnol-ai-analysis` | Analysis results keyed by claim and analysis | `BedrockGenAiService.analyzeClaim` returns an `AiAnalysisResult` from local heuristic logic; it does not persist the result. |
| `fnol-processed-events` | Potential deduplication ledger | No read/write or conditional-write call; event de-duplication is not implemented. |

These are not actual table ownership relationships in the running application; only IaC declares the resources. The schema names and some intended purposes are also described in `docs/database/dynamodb-model.md`, but the document does not establish application access.

## Provisioning differences

- `infrastructure/docker/localstack/init-aws.sh` and `infrastructure/dynamodb/create-tables.sh` create six tables with the keys in the table above, using on-demand billing. Neither script creates GSIs, enables TTL, or enables DynamoDB Streams.
- Terraform declares the same six table names and base key schemas. `infrastructure/terraform/modules/dynamodb/main.tf` configures point-in-time recovery, optional deletion protection, optional TTL, GSIs, and optional streams.
- Terraform's default table list has GSIs on `fnol-claims` and `fnol-processing-status`; the dev/test/prod environment definitions similarly pass these GSI declarations. The Terraform module sets the table name directly to the configured name (no environment prefix on table names).
- The Terraform table module enables server-side encryption, with an optional configured KMS key ARN. This does not imply Java data access.

## Direct database access versus streams

There is no DynamoDB Streams producer/consumer flow:

- All configured tables have `stream_enabled=false`.
- No Lambda function, `aws_lambda_event_source_mapping`, Streams event handler, or stream consumer code exists.
- No application code subscribes to table changes.

Even when enabled, direct DynamoDB `GetItem`/`PutItem`/`Query` access is request/response data access, not an event-driven communication path. In this repository, neither direct database operations nor Streams-driven processing are implemented.

### DynamoDB Streams producer table

| Source table | Stream enabled? | Stream view type | Change-event producer |
|---|---|---|---|
| `fnol-claims` | No | None | None |
| `fnol-claim-events` | No | None | None |
| `fnol-documents` | No | None | None |
| `fnol-processing-status` | No | None | None |
| `fnol-ai-analysis` | No | None | None |
| `fnol-processed-events` | No | None | None |

### DynamoDB Streams consumer table

| Consumer repository/module | Handler / method | POM | Event source mapping / checkpoint | Database or downstream effect |
|---|---|---|---|---|
| None | None | None | None | No Streams-triggered processing, DynamoDB updates, or next event |

Terraform's DynamoDB module supports an optional stream setting and view type, but the supplied table definitions do not enable it. There is no Lambda or other event-source mapping that would consume such a stream.

## Conditional writes, concurrency, and idempotency

No DynamoDB conditional expression, conditional write, optimistic locking attribute/version, transaction, or idempotency write exists in Java. The table definitions alone do not provide idempotency. The `fnol-processed-events` schema could support an event ledger in a future implementation, but it is not used.

## Relevant files

- `infrastructure/terraform/variables.tf`: default table list, keys, GSIs, TTL and Streams defaults.
- `infrastructure/terraform/modules/dynamodb/main.tf`: actual Terraform table resource settings.
- `infrastructure/terraform/environments/dev/main.tf`, `test/main.tf`, `prod/main.tf`: per-environment table list.
- `infrastructure/docker/localstack/init-aws.sh` and `infrastructure/dynamodb/create-tables.sh`: local table creation.
- `fnol-api-service/src/main/java/com/fnol/api/config/AwsConfiguration.java`: unused DynamoDB client bean.
- `fnol-api-service/src/main/java/com/fnol/api/service/ClaimService.java`, `fnol-document-service/src/main/java/com/fnol/document/service/DocumentProcessingService.java`, `fnol-event-processor/src/main/java/com/fnol/processor/service/ClaimEventProcessorService.java`, and `fnol-query-service/src/main/java/com/fnol/query/service/ClaimQueryService.java`: in-memory maps, not DynamoDB repositories.

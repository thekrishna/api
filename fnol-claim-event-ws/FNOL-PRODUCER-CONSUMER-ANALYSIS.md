# FNOL Producer–Consumer and AWS Component Analysis

## Scope and evidence standard

The six named services are Maven modules in the single `fnol-claim-event-ws` repository; they are not six separate repositories in this checkout. This review covers the root and all six module POMs, Java production sources, service `application.yml` files, LocalStack/Docker configuration, and Terraform.

“Implemented” below means a Java call path exists in source. A resource declared by a script or Terraform is not treated as an application integration. Existing architecture notes sometimes describe intended consumers and table access that are not present in the Java source.

## Executive summary

- **Implemented AWS data path:** `fnol-api-service` accepts `POST /api/v1/claims`, creates an in-memory claim, and calls `KinesisClaimPublisher.publish` to synchronously issue Kinesis `PutRecord` on `fnol-claim-events`.
- **Important payload detail:** the API constructs a `ClaimEventEnvelope<ClaimRecord>`, but the publisher sends only the text `eventType|claimId|timestamp`; the envelope payload is not serialized to Kinesis.
- **No Kinesis consumer is implemented.** `ClaimEventProcessorService.processEvent` contains business logic but is not wired to Kinesis or invoked by another service.
- **No Java SQS producer or consumer is implemented.** A local script attempts to create a standard queue named `fnol-claim-dlq`; Terraform contains DLQ variables but no queue resource or DLQ wiring.
- **No Java DynamoDB read/write operation is implemented.** The API creates an unused `DynamoDbClient` bean; the other relevant POMs declare DynamoDB dependencies but have no DynamoDB call sites.
- **No DynamoDB Streams consumer, Lambda handler, Lambda event mapping, KCL consumer, checkpointing, or replay implementation is present.**
- Claim, event, document, processor, and query data represented in Java is held in process-local maps. It is not shared across services and is lost on restart.
- The policy validation, adjuster assignment, damage assessment, settlement, approval, payment, notification, and replay workflows are not connected end to end.

## Maven project and AWS dependency inventory

AWS SDK version is centrally managed as **AWS SDK for Java v2, `2.25.60`**, through the `software.amazon.awssdk:bom` import in the root POM. Spring Boot is managed as `3.3.5`. No AWS SDK v1 dependencies were found.

| Maven project/module | Exact POM | AWS dependencies | Internal Maven dependency |
|---|---|---|---|
| Parent aggregator `fnol-claim-event-ws` | `pom.xml` | AWS SDK v2 BOM `2.25.60`; not an AWS service client itself | Aggregates all six modules |
| `fnol-common` | `fnol-common/pom.xml` | None | Shared module |
| `fnol-api-service` | `fnol-api-service/pom.xml` | SDK v2 `kinesis`, `dynamodb` | `com.fnol:fnol-common` |
| `fnol-document-service` | `fnol-document-service/pom.xml` | SDK v2 `kinesis`, `dynamodb` | `com.fnol:fnol-common` |
| `fnol-event-processor` | `fnol-event-processor/pom.xml` | SDK v2 `kinesis`, `dynamodb` | `com.fnol:fnol-common` |
| `fnol-genai-service` | `fnol-genai-service/pom.xml` | None; no Bedrock SDK | `com.fnol:fnol-common` |
| `fnol-query-service` | `fnol-query-service/pom.xml` | None | `com.fnol:fnol-common` |

No POM declares AWS SDK v1, SQS client dependencies, AWS Lambda libraries, or Spring Cloud AWS. `fnol-common` provides domain/event DTOs, not an AWS client or messaging abstraction. No service module depends on another service module.

## Producer–consumer mapping

| Business module | Producer repository / module | Producer class and method | Producer POM | AWS component and resource | Consumer repository / module | Consumer class and method | Consumer POM / result |
|---|---|---|---|---|---|---|---|
| FNOL submission | `fnol-claim-event-ws` / `fnol-api-service` | `ClaimService.createClaim` → `KinesisClaimPublisher.publish` | `fnol-api-service/pom.xml` | Kinesis `fnol-claim-events`; `PutRecord` | None wired. The processor module has logic only. | `ClaimEventProcessorService.processEvent` is not subscribed or called | `fnol-event-processor/pom.xml`; no record read/checkpoint |
| Policy validation | None | None | — | No validation API/resource connected | None | No policy consumer/validator | — |
| Claim creation | `fnol-claim-event-ws` / `fnol-api-service` | `ClaimController.submitClaim` → `ClaimService.createClaim` | `fnol-api-service/pom.xml` | In-memory `ConcurrentHashMap`; then Kinesis publication | None | No consumer | — |
| Adjuster assignment | None | None | — | No AWS resource or event observed | None | None | — |
| Damage assessment | None for a business workflow | `BedrockGenAiService.analyzeClaim` is callable service logic only | `fnol-genai-service/pom.xml` | No AWS component; implementation uses local string checks | None wired | No trigger/caller found | — |
| Settlement calculation | None | None | — | No AWS resource or event observed | None | None | — |
| Claim approval | None | `ClaimController.updateClaimStatus` can accept a caller-supplied status, but is not an approval workflow | `fnol-api-service/pom.xml` | In-memory update; status event kept in memory only | None | No event consumer | — |
| Payment processing | None | None | — | No AWS resource or event observed | None | None | — |
| Claim settlement | None | None | — | No AWS resource or event observed | None | None | — |
| Customer notification | None | None | — | No AWS resource or event observed | None | None | — |
| Replay administration | None | `GET /api/v1/claims/{claimId}/events` is an in-memory history read, not replay | `fnol-api-service/pom.xml` | No AWS replay path | None | No replay handler | — |

## Kinesis producers and consumers

| Role | Repository / module | Java class and method | POM | Stream / application name | Details |
|---|---|---|---|---|---|
| Producer | `fnol-claim-event-ws` / `fnol-api-service` | `KinesisClaimPublisher.publish` called by `ClaimService.createClaim` | `fnol-api-service/pom.xml` | `fnol-claim-events` by default; overridden by `KINESIS_STREAM_FNOL` | AWS SDK v2 synchronous `KinesisClient.putRecord`; partition key is `claimId` |
| Consumer | None | None | — | No application/consumer-group name configured | No `GetRecords`, shard iterator, `SubscribeToShard`, KCL, Spring adapter, Lambda Kinesis handler, or checkpoint implementation |

The API constructs a `ClaimEventEnvelope<ClaimRecord>` with `eventType=CLAIM_SUBMITTED`, source `fnol-api-service`, version `1.0`, generated event ID, and the `ClaimRecord` payload. However, `KinesisClaimPublisher.publish` sends only `eventType|claimId|timestamp`. `fnol-common/src/main/java/com/fnol/common/ClaimEventEnvelope.java` defines the envelope; `fnol-common/src/main/java/com/fnol/common/EventType.java` defines event names.

The three streams in LocalStack/Terraform are `fnol-claim-events`, `fnol-document-events`, and `fnol-processing-events`. Only the first has a Java producer. The other two are provisioned resources without a Java producer or consumer in this checkout.

## SQS producers and consumers

| Role | Java class / method | Project POM | Queue name/URL | Message / behavior |
|---|---|---|---|---|
| Producer | None | None | None in Java | No `SqsClient`, `SqsAsyncClient`, send operation, or queue abstraction |
| Consumer | None | None | None in Java | No listener, polling, receive/delete, handler, or Lambda event handler |
| Local resource only | `infrastructure/docker/localstack/init-aws.sh`; also `infrastructure/dynamodb/create-tables.sh` | Not a Maven project | `fnol-claim-dlq` | Script calls `create-queue` without queue attributes; it does not attach a redrive policy or process messages |

The local queue name has no `.fifo` suffix and scripts do not set FIFO attributes, message-group IDs, or deduplication IDs; it is therefore intended as a standard queue. Visibility timeout, retention, receive count, redrive policy, and retry policy are not configured by these queue creation calls. Terraform variables `enable_dead_letter_queue` and `dlq_name` are declared and set in some environment files, but no Terraform resource consumes them. The Terraform default `dlq_name` is `fnol-event-dlq`, different from the LocalStack queue `fnol-claim-dlq`.

`docker-compose.yml` configures LocalStack `SERVICES` as `dynamodb,kinesis,logs`, which omits SQS even though `init-aws.sh` attempts to create `fnol-claim-dlq`. The script suppresses that command's failure with `|| true`.

## DynamoDB access and stream status

No Java service owns or accesses the provisioned tables through an implemented DynamoDB operation. The API's `AwsConfiguration.dynamoDbClient` creates a client bean, but no code injects or calls it. The table inventory, keys, IaC-vs-code status, and stream configuration are detailed in [DYNAMODB-TABLE-OWNERSHIP.md](DYNAMODB-TABLE-OWNERSHIP.md).

Terraform defaults and LocalStack scripts disable DynamoDB Streams for every declared table. There is no stream event source mapping or Lambda consumer. Direct DynamoDB access (even if it were implemented) would be database I/O, not event-driven communication.

## Workflow and execution analysis

| Workflow | Starting surface and business method | Confirmed execution | Missing link / next event |
|---|---|---|---|
| FNOL submission | `ClaimController.submitClaim` → `ClaimService.createClaim` | Validates request DTO, stores claim and event in process memory, publishes Kinesis `CLAIM_SUBMITTED` record | No consumer connected; Kinesis data omits the DTO payload |
| Policy validation | No endpoint/validator | No policy lookup or policy service call | No `CLAIM_VALIDATED`/`CLAIM_REJECTED` publication |
| Claim creation | Same `createClaim` | Claim object is constructed and stored in local map | No DynamoDB persistence or downstream processor invocation |
| Adjuster assignment | No source implementation | Not implemented | No assignment message/event |
| Damage assessment | `BedrockGenAiService.analyzeClaim` | Local heuristic based on description text | No model/AWS Bedrock request, event trigger, persistence, or result publisher |
| Settlement calculation | No source implementation | Not implemented | No settlement event |
| Claim approval | `ClaimController.updateClaimStatus` → `ClaimService.updateStatus` | Caller-provided enum status updates local map; a status event is added to a local list | Status event is not sent to Kinesis/SQS |
| Payment processing | No source implementation | Not implemented | No payment service/provider/event |
| Claim settlement | No source implementation | `SETTLED` exists as a `ClaimStatus` value only | No settlement action/event |
| Customer notification | No source implementation | Not implemented | No notification provider or queue |
| Replay administration | `ClaimController.getClaimEvents` | Returns process-local event list | No replay command, Kinesis read, or re-publication |

## Independent execution, reliability, and ordering

- The five Spring Boot service modules can be started as separate applications, but the code does not establish an integrated distributed workflow. Docker Compose starts only LocalStack, `fnol-api-service`, and `fnol-query-service`.
- `fnol-api-service` performs synchronous inbound REST handling and a synchronous Kinesis SDK call. No backend service-to-service REST client was found.
- `fnol-event-processor`, `fnol-document-service`, `fnol-genai-service`, and `fnol-query-service` contain local service logic but have no implemented event subscription/trigger path. The query service's `saveClaim` method is not called by the API or processor.
- There are zero configured parallel Kinesis consumers, zero SQS consumers, and zero DynamoDB Streams consumers.
- The Kinesis producer uses the claim ID as partition key. This supplies a stable partition key for records for a claim, but no consumer-side ordering/checkpoint logic is implemented.
- `eventId` values are generated, but no deduplication store or conditional write is used. The declared `fnol-processed-events` table is not accessed.
- No application-level retry, backoff, dead-letter handling, replay, or idempotency logic was found. A successful response follows the synchronous `putRecord`; publication failure is not caught in `ClaimService.createClaim`.

## Infrastructure and configuration pointers

| Concern | Confirmed configuration |
|---|---|
| Application stream | `fnol-api-service/src/main/resources/application.yml`: `KINESIS_STREAM_FNOL`, default `fnol-claim-events` |
| AWS client setup | `fnol-api-service/src/main/java/com/fnol/api/config/AwsConfiguration.java`: `AWS_REGION` default `us-east-1`; optional `AWS_ENDPOINT_URL`; default credentials provider |
| Sample local environment | `.env.example`: AWS region/endpoint and names for three streams; does not configure service consumers or table names |
| LocalStack | `docker-compose.yml`; only DynamoDB, Kinesis, and logs listed in `SERVICES` |
| Kinesis/table provisioning | `infrastructure/docker/localstack/init-aws.sh`, `infrastructure/kinesis/create-streams.sh`, `infrastructure/dynamodb/create-tables.sh`, Terraform root and modules |
| IAM | `infrastructure/terraform/modules/iam/main.tf`: generic producer and DynamoDB policies; no consumer/Kinesis read, SQS, or Streams permissions |
| Lambda/CloudFormation | No Lambda source/config/resource, event source mapping, or CloudFormation template found |

## Existing-document discrepancies to keep in mind

The repository's earlier `docs/architecture/architecture-overview.md`, `docs/architecture/aws-architecture-diagram.md`, `docs/architecture/terraform-resource-mapping.md`, and README describe or imply service/database/event paths that are not implemented in Java. In particular, they depict a Kinesis consumer, document-event publisher, and DynamoDB reads/writes. This analysis treats source call sites and actual infrastructure definitions—not those narrative assertions—as implementation evidence.

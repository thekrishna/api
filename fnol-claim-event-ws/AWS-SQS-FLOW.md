# AWS SQS Flow Analysis

## Finding

There is **no Java SQS producer or consumer** in the six Maven modules. No service POM declares an SQS client or Spring Cloud AWS integration, and no source calls `SendMessage`, `ReceiveMessage`, or `DeleteMessage`, declares `@SqsListener`, implements a polling loop, or handles Lambda SQS events.

The only SQS evidence is infrastructure code attempting to create one queue:

| Resource | Name | Evidence | Application connection |
|---|---|---|---|
| LocalStack queue | `fnol-claim-dlq` | `infrastructure/docker/localstack/init-aws.sh`; also `infrastructure/dynamodb/create-tables.sh` | None: no producer, consumer, redrive policy, or queue URL property |
| Terraform DLQ configuration | `enable_dead_letter_queue`, `dlq_name` (default `fnol-event-dlq`) | `infrastructure/terraform/variables.tf`; environment main files set the boolean | No Terraform SQS resource references either variable; no queue is created by Terraform |

The LocalStack queue name and Terraform variable default differ. The queue is therefore not a confirmed Terraform deployment resource.

## SQS producer table

| Repository/module | Class/method | POM | Queue | Message / behavior |
|---|---|---|---|---|
| None | None | None | None | No SQS message DTO, send operation, message body, group ID, or deduplication ID |

## SQS consumer table

| Repository/module | Class/method | POM | Queue | Processing / retry |
|---|---|---|---|---|
| None | None | None | None | No listener, receive/delete handler, polling, Lambda handler, visibility handling, or retry processing |

## Infrastructure-only queue

| Resource | Repository/path | Queue | Attributes |
|---|---|---|---|
| LocalStack script resource | `fnol-claim-event-ws` / `infrastructure/docker/localstack/init-aws.sh`; also `infrastructure/dynamodb/create-tables.sh` | `fnol-claim-dlq` | No queue attributes supplied; no application connection |
| Terraform variables only | `infrastructure/terraform/variables.tf` and environment files | Default variable says `fnol-event-dlq` | No SQS queue resource consumes the values |

## Queue type and reliability configuration

The local queue name is not suffixed `.fifo`; the shell creation calls provide no FIFO attributes. It is configured as a standard queue by the available evidence. The scripts do not set:

- Visibility timeout or message retention.
- Redrive policy, maximum receive count, or source queue association.
- Delivery delay or long-poll wait time.
- FIFO message group/deduplication IDs.
- Any consumer retry, deletion, or poison-message behavior.

No Java service reads environment variables for an SQS URL or queue name. No Terraform resource creates or configures an SQS queue. `enable_dead_letter_queue` and `dlq_name` are currently declarative variables only.

## LocalStack configuration mismatch

`docker-compose.yml` configures LocalStack with `SERVICES=dynamodb,kinesis,logs`, which excludes SQS. Nevertheless, `infrastructure/docker/localstack/init-aws.sh` attempts to create `fnol-claim-dlq`. That command has `|| true`, so SQS creation failure is ignored and does not stop initialization. The separate `infrastructure/dynamodb/create-tables.sh` also attempts the queue without a corresponding consumer.

The README's claim that LocalStack initializes a queue should therefore not be treated as proof that the queue is available: the configured LocalStack service list does not include SQS.

## Event-flow assessment

| Candidate flow | Status |
|---|---|
| Kinesis → SQS | Not implemented; no Kinesis consumer or SQS send operation |
| SQS → Kinesis | Not implemented; no SQS consumer or Kinesis publisher bridge |
| Kinesis failure → SQS DLQ | Not implemented; no consumer, error handler, source mapping, or redrive policy |
| SQS → Lambda | Not implemented; no Lambda handler/event-source mapping |
| SQS retry / DLQ replay | Not implemented |

Accordingly, SQS does not participate in FNOL submission, claim processing, document/GenAI work, notifications, or replay in the Java application found in this checkout.

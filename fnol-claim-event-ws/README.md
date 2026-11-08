# FNOL Claim Event Platform

This project implements a production-ready FNOL claims platform using Java 21, Spring Boot 3.x, AWS Kinesis, DynamoDB, and a React + TypeScript dashboard. The solution is designed for LocalStack-first local development and supports AWS-backed deployments via environment variables.

## Modules

- `fnol-common`: shared domain models and event contracts
- `fnol-api-service`: REST API for claim intake and processing commands
- `fnol-event-processor`: event consumer and processing orchestration
- `fnol-document-service`: document metadata and upload processing
- `fnol-genai-service`: AI analysis abstraction with Bedrock-style provider implementation
- `fnol-query-service`: claim query and dashboard endpoints
- `frontend`: React + TypeScript user interface
- `infrastructure`: DynamoDB scripts, Kinesis scripts, LocalStack setup

## Quick Start (Windows PowerShell)

```powershell
$env:JAVA_HOME = 'D:\jdk-21.0.6'
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
Set-Location 'D:\GenAi-Projects\fnol-claim-event-ws'

mvn clean test

# Optional LocalStack setup
cd infrastructure\dynamodb
./create-tables.ps1

# Frontend
cd ..\..\frontend
npm install
npm run build
```

## LocalStack AWS resources

The LocalStack bootstrap creates the Kinesis streams, DynamoDB tables, and the SQS dead-letter queue used by the event pipeline.

```bash
./infrastructure/docker/localstack/init-aws.sh
```

The queue created is:

```text
http://localhost:4566/000000000000/fnol-claim-dlq
```

The DynamoDB bootstrap script also creates the same DLQ when provisioning local infrastructure:

```bash
./infrastructure/dynamodb/create-tables.sh
```

## Docker Compose

```powershell
docker compose up -d
```

## Architecture

See the docs folder for architecture and schema documentation.

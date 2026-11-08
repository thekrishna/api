# AWS Application Configuration Integration

This document explains how the Terraform infrastructure connects to the existing FNOL Java services without modifying the application model beyond existing configuration values.

## Existing configuration contract

The current application already exposes the stream name through Spring configuration:

```yaml
aws:
  kinesis:
    fnol-claim-events-stream: ${KINESIS_STREAM_FNOL:fnol-claim-events}
```

The Terraform outputs map to this contract.

## Recommended environment variables

```bash
AWS_REGION=us-east-1
KINESIS_STREAM_FNOL=fnol-claim-events
FNOL_CLAIMS_TABLE=fnol-claims
FNOL_CLAIM_EVENTS_TABLE=fnol-claim-events
FNOL_DOCUMENTS_TABLE=fnol-documents
FNOL_PROCESSING_STATUS_TABLE=fnol-processing-status
FNOL_AI_ANALYSIS_TABLE=fnol-ai-analysis
FNOL_PROCESSED_EVENTS_TABLE=fnol-processed-events
```

## Connection pattern

- The FNOL API service writes claim events to the Kinesis stream named by `KINESIS_STREAM_FNOL`.
- The event processor and query service consume or query the DynamoDB tables by their own table name variables and IAM role permissions.
- LocalStack uses the same table and stream names for local validation.
- Real AWS deployments should keep the same names to avoid application-side changes.

## Security note

The application should assume an IAM role rather than embedding AWS credentials in `application.yml` or environment files. The Terraform module creates an application role that can be attached to ECS tasks or EC2 instances.

## Use of output values

Terraform outputs can be consumed by deployment runners and CI jobs to wire environment variables into the Java services without hardcoding AWS identifiers in application code.

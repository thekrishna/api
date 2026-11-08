#!/usr/bin/env bash
set -euo pipefail

export AWS_REGION="${AWS_REGION:-us-east-1}"
export AWS_DEFAULT_REGION="${AWS_DEFAULT_REGION:-us-east-1}"

# Wait for LocalStack to be ready
until awslocal dynamodb list-tables >/dev/null 2>&1; do
  echo "Waiting for LocalStack DynamoDB..."
  sleep 2
done

# Create Kinesis streams
awslocal kinesis create-stream --stream-name fnol-claim-events --shard-count 1 || true
awslocal kinesis create-stream --stream-name fnol-document-events --shard-count 1 || true
awslocal kinesis create-stream --stream-name fnol-processing-events --shard-count 1 || true

# Create SQS dead-letter queue
awslocal sqs create-queue --queue-name fnol-claim-dlq || true

# Create DynamoDB tables
awslocal dynamodb create-table --table-name fnol-claims --attribute-definitions AttributeName=claimId,AttributeType=S --key-schema AttributeName=claimId,KeyType=HASH --billing-mode PAY_PER_REQUEST || true
awslocal dynamodb create-table --table-name fnol-claim-events --attribute-definitions AttributeName=claimId,AttributeType=S AttributeName=eventTimestampEventId,AttributeType=S --key-schema AttributeName=claimId,KeyType=HASH AttributeName=eventTimestampEventId,KeyType=RANGE --billing-mode PAY_PER_REQUEST || true
awslocal dynamodb create-table --table-name fnol-documents --attribute-definitions AttributeName=claimId,AttributeType=S AttributeName=documentId,AttributeType=S --key-schema AttributeName=claimId,KeyType=HASH AttributeName=documentId,KeyType=RANGE --billing-mode PAY_PER_REQUEST || true
awslocal dynamodb create-table --table-name fnol-processing-status --attribute-definitions AttributeName=claimId,AttributeType=S --key-schema AttributeName=claimId,KeyType=HASH --billing-mode PAY_PER_REQUEST || true
awslocal dynamodb create-table --table-name fnol-ai-analysis --attribute-definitions AttributeName=claimId,AttributeType=S AttributeName=analysisId,AttributeType=S --key-schema AttributeName=claimId,KeyType=HASH AttributeName=analysisId,KeyType=RANGE --billing-mode PAY_PER_REQUEST || true
awslocal dynamodb create-table --table-name fnol-processed-events --attribute-definitions AttributeName=eventId,AttributeType=S --key-schema AttributeName=eventId,KeyType=HASH --billing-mode PAY_PER_REQUEST || true

echo "LocalStack infrastructure initialized."

#!/usr/bin/env bash
set -euo pipefail

AWS_REGION="${AWS_REGION:-us-east-1}"
AWS_ENDPOINT_URL="${AWS_ENDPOINT_URL:-}"
PROFILE_ARG=()
CMD_PREFIX=(aws)

if [[ -n "${AWS_PROFILE:-}" ]]; then
  PROFILE_ARG=(--profile "$AWS_PROFILE")
fi

if [[ -n "$AWS_ENDPOINT_URL" ]]; then
  CMD_PREFIX=(aws --endpoint-url "$AWS_ENDPOINT_URL")
fi

function table_exists() {
  local table_name="$1"
  "${CMD_PREFIX[@]}" dynamodb list-tables "${PROFILE_ARG[@]}" --region "$AWS_REGION" --output text | grep -q "$table_name"
}

function create_table() {
  local table_name="$1"
  local key_schema="$2"
  local attribute_definitions="$3"
  local provisioned="$4"
  if table_exists "$table_name"; then
    echo "Table already exists: $table_name"
    return
  fi
  echo "Creating table: $table_name"
  eval "${CMD_PREFIX[@]} dynamodb create-table ${PROFILE_ARG[@]} --region \"$AWS_REGION\" --table-name \"$table_name\" --attribute-definitions $attribute_definitions --key-schema $key_schema --billing-mode PAY_PER_REQUEST ${provisioned}"
}

create_table "fnol-claims" \
  'AttributeName=claimId,KeyType=HASH' \
  'AttributeName=claimId,StringType' \
  'ProvisionedThroughput ReadCapacityUnits=5,WriteCapacityUnits=5'

create_table "fnol-claim-events" \
  'AttributeName=claimId,KeyType=HASH AttributeName=eventTimestampEventId,KeyType=RANGE' \
  'AttributeName=claimId,StringType AttributeName=eventTimestampEventId,StringType' \
  'ProvisionedThroughput ReadCapacityUnits=5,WriteCapacityUnits=5'

create_table "fnol-documents" \
  'AttributeName=claimId,KeyType=HASH AttributeName=documentId,KeyType=RANGE' \
  'AttributeName=claimId,StringType AttributeName=documentId,StringType' \
  'ProvisionedThroughput ReadCapacityUnits=5,WriteCapacityUnits=5'

create_table "fnol-processing-status" \
  'AttributeName=claimId,KeyType=HASH' \
  'AttributeName=claimId,StringType' \
  'ProvisionedThroughput ReadCapacityUnits=5,WriteCapacityUnits=5'

create_table "fnol-ai-analysis" \
  'AttributeName=claimId,KeyType=HASH AttributeName=analysisId,KeyType=RANGE' \
  'AttributeName=claimId,StringType AttributeName=analysisId,StringType' \
  'ProvisionedThroughput ReadCapacityUnits=5,WriteCapacityUnits=5'

create_table "fnol-processed-events" \
  'AttributeName=eventId,KeyType=HASH' \
  'AttributeName=eventId,StringType' \
  'ProvisionedThroughput ReadCapacityUnits=5,WriteCapacityUnits=5'

if ! "${CMD_PREFIX[@]}" sqs list-queues "${PROFILE_ARG[@]}" --region "$AWS_REGION" --queue-name-prefix fnol-claim-dlq --output text | grep -q 'fnol-claim-dlq'; then
  echo "Creating SQS queue: fnol-claim-dlq"
  "${CMD_PREFIX[@]}" sqs create-queue "${PROFILE_ARG[@]}" --region "$AWS_REGION" --queue-name fnol-claim-dlq
else
  echo "SQS queue already exists: fnol-claim-dlq"
fi

echo "Waiting for tables to become ACTIVE..."
for table in fnol-claims fnol-claim-events fnol-documents fnol-processing-status fnol-ai-analysis fnol-processed-events; do
  until "${CMD_PREFIX[@]}" dynamodb describe-table "${PROFILE_ARG[@]}" --region "$AWS_REGION" --table-name "$table" --query 'Table.TableStatus' --output text | grep -q ACTIVE; do
    echo "Waiting on $table..."
    sleep 2
  done
done

echo "All DynamoDB tables are active."

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

for table in fnol-claims fnol-claim-events fnol-documents fnol-processing-status fnol-ai-analysis fnol-processed-events; do
  echo "Deleting table: $table"
  "${CMD_PREFIX[@]}" dynamodb delete-table "${PROFILE_ARG[@]}" --region "$AWS_REGION" --table-name "$table" || true
done

echo "Deletion commands issued."

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

for stream in fnol-claim-events fnol-document-events fnol-processing-events; do
  echo "Ensuring stream exists: $stream"
  "${CMD_PREFIX[@]}" kinesis create-stream "${PROFILE_ARG[@]}" --region "$AWS_REGION" --stream-name "$stream" --shard-count 1 || true
done

echo "Kinesis streams ready."

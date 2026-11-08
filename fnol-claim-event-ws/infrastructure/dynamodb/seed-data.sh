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

"${CMD_PREFIX[@]}" dynamodb put-item "${PROFILE_ARG[@]}" --region "$AWS_REGION" --table-name fnol-claims --item '{"claimId":{"S":"CLM-1000"},"policyNumber":{"S":"POL-9001"},"claimantId":{"S":"CUS-2001"},"claimantName":{"S":"Jane Doe"},"incidentDate":{"S":"2026-11-08"},"incidentLocation":{"S":"Austin, TX"},"claimType":{"S":"PROPERTY"},"claimDescription":{"S":"Roof damage from hail storm"},"claimAmount":{"N":"15400.00"},"claimStatus":{"S":"SUBMITTED"},"createdAt":{"S":"2026-11-08T12:00:00Z"},"updatedAt":{"S":"2026-11-08T12:00:00Z"}}'

echo "Sample data inserted."

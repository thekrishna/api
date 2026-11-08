$ErrorActionPreference = 'Stop'

$region = if ($env:AWS_REGION) { $env:AWS_REGION } else { 'us-east-1' }
$endpoint = $env:AWS_ENDPOINT_URL
$profile = $env:AWS_PROFILE

function Invoke-DynamoCli($arguments) {
    $cmd = @('aws')
    if ($profile) { $cmd += @('--profile', $profile) }
    if ($endpoint) { $cmd += @('--endpoint-url', $endpoint) }
    $cmd += $arguments
    & $cmd[0] $cmd[1..($cmd.Length-1)]
    if ($LASTEXITCODE -ne 0) { throw "AWS CLI command failed: $($cmd -join ' ')" }
}

Invoke-DynamoCli @(
    'dynamodb','put-item',
    '--table-name','fnol-claims',
    '--region',$region,
    '--item',
    '{"claimId":{"S":"CLM-1000"},"policyNumber":{"S":"POL-9001"},"claimantId":{"S":"CUS-2001"},"claimantName":{"S":"Jane Doe"},"incidentDate":{"S":"2026-11-08"},"incidentLocation":{"S":"Austin, TX"},"claimType":{"S":"PROPERTY"},"claimDescription":{"S":"Roof damage from hail storm"},"claimAmount":{"N":"15400.00"},"claimStatus":{"S":"SUBMITTED"},"createdAt":{"S":"2026-11-08T12:00:00Z"},"updatedAt":{"S":"2026-11-08T12:00:00Z"}}'
)

Write-Host 'Sample data inserted.'

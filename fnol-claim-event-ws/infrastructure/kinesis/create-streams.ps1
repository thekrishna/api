$ErrorActionPreference = 'Stop'

$region = if ($env:AWS_REGION) { $env:AWS_REGION } else { 'us-east-1' }
$endpoint = $env:AWS_ENDPOINT_URL
$profile = $env:AWS_PROFILE

function Invoke-KinesisCli($arguments) {
    $cmd = @('aws')
    if ($profile) { $cmd += @('--profile', $profile) }
    if ($endpoint) { $cmd += @('--endpoint-url', $endpoint) }
    $cmd += $arguments
    & $cmd[0] $cmd[1..($cmd.Length-1)]
    if ($LASTEXITCODE -ne 0) { throw "AWS CLI command failed: $($cmd -join ' ')" }
}

foreach ($stream in @('fnol-claim-events', 'fnol-document-events', 'fnol-processing-events')) {
    Write-Host "Ensuring Kinesis stream exists: $stream"
    Invoke-KinesisCli @('kinesis','create-stream','--stream-name',$stream,'--shard-count','1','--region',$region)
}

Write-Host 'Kinesis stream initialization complete.'

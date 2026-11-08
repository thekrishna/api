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

function Test-TableExists($tableName) {
    $result = Invoke-DynamoCli @('dynamodb','list-tables','--region',$region,'--output','text') 2>$null
    return $result -match $tableName
}

$tables = @(
    @{ Name='fnol-claims'; KeySchema='AttributeName=claimId,KeyType=HASH'; Attributes='AttributeName=claimId,StringType'; },
    @{ Name='fnol-claim-events'; KeySchema='AttributeName=claimId,KeyType=HASH AttributeName=eventTimestampEventId,KeyType=RANGE'; Attributes='AttributeName=claimId,StringType AttributeName=eventTimestampEventId,StringType'; },
    @{ Name='fnol-documents'; KeySchema='AttributeName=claimId,KeyType=HASH AttributeName=documentId,KeyType=RANGE'; Attributes='AttributeName=claimId,StringType AttributeName=documentId,StringType'; },
    @{ Name='fnol-processing-status'; KeySchema='AttributeName=claimId,KeyType=HASH'; Attributes='AttributeName=claimId,StringType'; },
    @{ Name='fnol-ai-analysis'; KeySchema='AttributeName=claimId,KeyType=HASH AttributeName=analysisId,KeyType=RANGE'; Attributes='AttributeName=claimId,StringType AttributeName=analysisId,StringType'; },
    @{ Name='fnol-processed-events'; KeySchema='AttributeName=eventId,KeyType=HASH'; Attributes='AttributeName=eventId,StringType'; }
)

foreach ($table in $tables) {
    if (Test-TableExists $table.Name) {
        Write-Host "Table already exists: $($table.Name)"
        continue
    }

    Write-Host "Creating table: $($table.Name)"
    Invoke-DynamoCli @(
        'dynamodb','create-table',
        '--table-name',$table.Name,
        '--attribute-definitions',$table.Attributes,
        '--key-schema',$table.KeySchema,
        '--billing-mode','PAY_PER_REQUEST',
        '--region',$region
    )
}

foreach ($tableName in $tables.Name) {
    do {
        Start-Sleep -Seconds 2
        $status = Invoke-DynamoCli @('dynamodb','describe-table','--table-name',$tableName,'--region',$region,'--query','Table.TableStatus','--output','text')
        Write-Host "Waiting on $tableName: $status"
    } while ($status -notmatch 'ACTIVE')
}

Write-Host 'All DynamoDB tables are active.'

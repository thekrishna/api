param(
    [ValidateSet('dev', 'test', 'prod')]
    [string]$Environment = 'dev',
    [switch]$Upgrade
)

$ErrorActionPreference = 'Stop'

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot '..')
$envDir = Join-Path $repoRoot "environments/$Environment"

if (-not (Test-Path $envDir)) {
    throw "Environment directory not found: $envDir"
}

if (-not (Get-Command terraform -ErrorAction SilentlyContinue)) {
    throw 'Terraform is not installed or not on PATH.'
}

if (-not (Get-Command aws -ErrorAction SilentlyContinue)) {
    throw 'AWS CLI is not installed. Install it with: winget install --id Amazon.AWSCLI -e --source winget'
}

$commandArgs = @('-chdir=' + $envDir, 'init')
if ($Upgrade) {
    $commandArgs += '-upgrade'
}

Write-Host "Initializing Terraform for environment: $Environment"
& terraform $commandArgs
if ($LASTEXITCODE -ne 0) {
    throw "terraform init failed for $Environment"
}

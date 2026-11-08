param(
    [ValidateSet('dev', 'test', 'prod')]
    [string]$Environment = 'dev',
    [string]$VarFile
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

Write-Host "Review the plan before applying changes to $Environment."
$confirmation = Read-Host "Type APPLY to continue"
if ($confirmation -ne 'APPLY') {
    throw 'Apply cancelled by user.'
}

$commandArgs = @('-chdir=' + $envDir, 'apply', '-auto-approve')
if ($VarFile) {
    $commandArgs += @('-var-file=' + $VarFile)
}

& terraform $commandArgs
if ($LASTEXITCODE -ne 0) {
    throw "terraform apply failed for $Environment"
}

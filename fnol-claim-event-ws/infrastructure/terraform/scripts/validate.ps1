param(
    [ValidateSet('dev', 'test', 'prod')]
    [string]$Environment = 'dev'
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

Write-Host 'Running terraform fmt -check -recursive'
& terraform -chdir=$repoRoot fmt -check -recursive
if ($LASTEXITCODE -ne 0) {
    throw 'terraform fmt failed'
}

Write-Host 'Running terraform init -backend=false'
& terraform -chdir=$repoRoot init -backend=false
if ($LASTEXITCODE -ne 0) {
    throw 'terraform init failed'
}

Write-Host 'Running terraform validate'
& terraform -chdir=$envDir validate
if ($LASTEXITCODE -ne 0) {
    throw 'terraform validate failed'
}

if (Get-Command tflint -ErrorAction SilentlyContinue) {
    Write-Host 'Running tflint'
    & tflint --chdir=$repoRoot
    if ($LASTEXITCODE -ne 0) {
        throw 'tflint failed'
    }
}

Write-Host 'Terraform validation completed successfully.'

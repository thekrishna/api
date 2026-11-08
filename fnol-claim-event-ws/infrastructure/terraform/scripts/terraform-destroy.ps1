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

$confirmation = Read-Host "Type DESTROY to confirm removal of all infrastructure in $Environment"
if ($confirmation -ne 'DESTROY') {
    throw 'Destroy cancelled by user.'
}

$commandArgs = @('-chdir=' + $envDir, 'destroy', '-auto-approve')
if ($VarFile) {
    $commandArgs += @('-var-file=' + $VarFile)
}

& terraform $commandArgs
if ($LASTEXITCODE -ne 0) {
    throw "terraform destroy failed for $Environment"
}

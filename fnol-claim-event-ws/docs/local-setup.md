# Local Setup Guide

## Prerequisites

- Java 21
- Maven 3.9+
- Docker Desktop
- Node.js 20+
- PowerShell 7+

## Start LocalStack

```powershell
docker compose up -d
```

## Initialize infrastructure

```powershell
Set-Location infrastructure\dynamodb
./create-tables.ps1
./seed-data.ps1
```

## Run backend services

```powershell
$env:JAVA_HOME='D:\jdk-21.0.6'
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
mvn clean test
```

## Run frontend

```powershell
Set-Location frontend
npm install
npm run dev
```

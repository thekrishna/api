# FNOL Architecture Overview

```mermaid
flowchart LR
    UI[React Frontend] --> API[FNOL API Service]
    API -->|Submit Claim| DDB[(DynamoDB)]
    API -->|Kinesis Event| K1[(Kinesis: fnol-claim-events)]
    K1 --> PROC[Event Processor]
    PROC --> DDB
    PROC --> Q[Query Service]
    DOC[Document Service] -->|Document event| K2[(Kinesis: fnol-document-events)]
    AI[GenAI Service] --> DDB
    Q --> UI
```

## Components

- FNOL API Service: receives REST requests and publishes claim-submitted events.
- Event Processor: consumes Kinesis events, updates claim status, and persists processing metadata.
- Document Service: manages metadata for uploaded claim documents and emits document events.
- GenAI Service: categorizes claim content and assesses risk indicators.
- Query Service: serves dashboard summaries and claim lookups.

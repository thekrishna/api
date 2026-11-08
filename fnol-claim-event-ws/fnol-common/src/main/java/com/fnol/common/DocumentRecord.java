package com.fnol.common;

import java.time.Instant;

public record DocumentRecord(
        String claimId,
        String documentId,
        DocumentType documentType,
        String fileName,
        String contentType,
        String storageLocation,
        String processingStatus,
        String extractedMetadata,
        Instant uploadedAt
) {
}

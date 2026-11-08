package com.fnol.document.service;

import com.fnol.common.DocumentRecord;
import com.fnol.common.DocumentType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentProcessingService {

    private final Map<String, List<DocumentRecord>> documents = new ConcurrentHashMap<>();

    public DocumentRecord uploadDocument(String claimId, DocumentType type, String fileName, String contentType) {
        String documentId = "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        DocumentRecord record = new DocumentRecord(
                claimId,
                documentId,
                type,
                fileName,
                contentType,
                "s3://fnol-documents/" + fileName,
                "UPLOADED",
                "{\"source\":\"manual-upload\"}",
                Instant.now()
        );
        documents.computeIfAbsent(claimId, key -> new java.util.ArrayList<>()).add(record);
        return record;
    }

    public List<DocumentRecord> getDocuments(String claimId) {
        return documents.getOrDefault(claimId, List.of());
    }
}

package com.fnol.document;

import com.fnol.common.DocumentType;
import com.fnol.document.service.DocumentProcessingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentProcessingServiceTest {

    @Test
    void shouldUploadDocument() {
        DocumentProcessingService service = new DocumentProcessingService();

        var document = service.uploadDocument("CLM-123", DocumentType.PHOTO, "roof.jpg", "image/jpeg");

        assertEquals("CLM-123", document.claimId());
        assertEquals("UPLOADED", document.processingStatus());
        assertEquals(1, service.getDocuments("CLM-123").size());
    }
}

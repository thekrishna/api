package com.insurance.vector.search.controller;

import com.insurance.vector.search.model.OcrDocumentRequest;
import com.insurance.vector.search.service.DocumentIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentIngestionController {

    private final DocumentIngestionService ingestionService;

    public DocumentIngestionController(final DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest-ocr")
    public ResponseEntity<String> ingest(@RequestBody final OcrDocumentRequest request) {
        ingestionService.ingest(request);
        return ResponseEntity.ok(
                "OCR document chunks and embeddings stored successfully"
        );
    }
}
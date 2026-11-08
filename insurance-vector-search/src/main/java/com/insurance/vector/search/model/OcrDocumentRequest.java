package com.insurance.vector.search.model;

import java.util.List;

public record OcrDocumentRequest(
        String documentId,
        String policyId,
        String applicantId,
        String lob,
        String documentType,
        List<OcrPage> pages
) {}

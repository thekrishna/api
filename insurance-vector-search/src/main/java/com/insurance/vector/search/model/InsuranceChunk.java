package com.insurance.vector.search.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "insurance_documents")
public class InsuranceChunk {
    @Id
    private String id;

    private String documentId;
    private String policyId;
    private String applicantId;

    private String lob;
    private String documentType;

    private Integer pageNumber;
    private Integer chunkNumber;

    private String content;

    private List<Double> embedding;
}